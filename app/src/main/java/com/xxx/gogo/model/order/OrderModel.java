package com.xxx.gogo.model.order;

import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.order.OrderEvent;
import com.xxx.gogo.manager.order.OrderManager;
import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderModel implements LowMemoryListener, OrderLocalDataSource.Callback {
    public static final int TYPE_ALL = 0;
    public static final int TYPE_PENDING = 1;
    public static final int TYPE_COMPLETED = 2;

    // need be locked by this
    private List<String> mAllOrderList;
    private List<String> mCompletedOrderList;
    private List<String> mPendingOrderList;
    private List<String> mCreatedOrderList;

    private Map<String, OrderItemInfo> mOrderMap;

    private OrderLocalDataSource mDataSource;
    private WeakReference<Callback> mCb;

    public static class InstanceHolder{
        private static OrderModel sInstance = new OrderModel();
    }

    public static OrderModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private OrderModel(){
        mOrderMap = new HashMap<>();
        mPendingOrderList = new ArrayList<>();
        mAllOrderList = new ArrayList<>();
        mCompletedOrderList = new ArrayList<>();
        mCreatedOrderList = new ArrayList<>();
        mDataSource = new OrderLocalDataSource(this);
    }

    public void setCallback(Callback cb){
        mCb = new WeakReference<>(cb);
    }

    public synchronized void add(final List<OrderItemInfo> orderList,
                                 final List<List<OrderItemDetailInfo>> detailList){
        /**
         * 保证add的时候，已经将处理中的订单load到内存
         */
        if(mOrderMap.isEmpty()){
            ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
                @Override
                public void run() {
                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            addInternal(orderList, detailList);
                        }
                    });
                }
            });
        }else {
            addInternal(orderList, detailList);
        }
    }

    private synchronized void addInternal(List<OrderItemInfo> orderList,
                                 List<List<OrderItemDetailInfo>> detailList){

        int i = 0;
        for (OrderItemInfo orderItemInfo : orderList){
            if(mOrderMap.containsKey(orderItemInfo.id)){
                //// TODO: 17/2/14 merge order
                //append to mOrderMap
                mDataSource.append(orderItemInfo, detailList.get(i));
            }else {
                mOrderMap.put(orderItemInfo.id, orderItemInfo);
                mAllOrderList.add(orderItemInfo.id);

                mDataSource.add(orderList, detailList);
            }
            i += 1;
        }
    }

    public synchronized void modifyState(String orderId, int state){
        if(mOrderMap.containsKey(orderId)){
            OrderItemInfo itemInfo = mOrderMap.get(orderId);
            if(itemInfo != null){
                itemInfo.state = state;
                switch (state){
                    case OrderItemInfo.STATE_PENDING:
                        mCreatedOrderList.remove(orderId);
                        mPendingOrderList.add(0, orderId);
                        break;
                    case OrderItemInfo.STATE_CANCELED:
                        mCreatedOrderList.remove(orderId);
                        break;
                    case OrderItemInfo.STATE_COMPLETED:
                        mPendingOrderList.remove(orderId);
                        mCompletedOrderList.add(0, orderId);
                        break;
                    default:
                        break;
                }

                mDataSource.modifyState(orderId, state);

                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        BusFactory.getBus().post(new OrderEvent.CheckOrderStateComplete(true));
                    }
                });
            }
        }
    }

    public void load(){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        mDataSource.load();
    }

    public boolean hasPendingOrders(){
        return mCreatedOrderList != null && mPendingOrderList != null &&
                !mCreatedOrderList.isEmpty() && !mPendingOrderList.isEmpty();
    }

    public synchronized int getCount(int type){
        switch (type){
            case TYPE_ALL:
                return mAllOrderList.size();
            case TYPE_COMPLETED:
                return mCompletedOrderList.size();
            case TYPE_PENDING:
                return mPendingOrderList.size();
            default:
                return 0;
        }
    }

    public synchronized OrderItemInfo getItem(int type, int position){
        switch (type){
            case TYPE_ALL:
                return mOrderMap.get(mAllOrderList.get(position));
            case TYPE_COMPLETED:
                return mOrderMap.get(mCompletedOrderList.get(position));
            case TYPE_PENDING:
                return mOrderMap.get(mPendingOrderList.get(position));
            default:
                return null;
        }
    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onLoaded(List<OrderItemInfo> orderItemInfoList) {
        Map<String, OrderItemInfo> map = new HashMap<>();
        List<String> pendingList = new ArrayList<>();
        List<String> completedList = new ArrayList<>();
        List<String> createdList = new ArrayList<>();
        List<String> allList = new ArrayList<>();

        for (OrderItemInfo orderItemInfo : orderItemInfoList){
            map.put(orderItemInfo.id, orderItemInfo);
            allList.add(orderItemInfo.id);

            if(orderItemInfo.state == OrderItemInfo.STATE_COMPLETED){
                completedList.add(orderItemInfo.id);
            }else if(orderItemInfo.state == OrderItemInfo.STATE_PENDING){
                pendingList.add(orderItemInfo.id);
            }else if (orderItemInfo.state == OrderItemInfo.STATE_CREATED){
                createdList.add(orderItemInfo.id);
            }
        }

        synchronized (this){
            mAllOrderList = allList;
            mPendingOrderList = pendingList;
            mCompletedOrderList = completedList;
            mCreatedOrderList = createdList;
            mOrderMap = map;
        }

        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                if(mCb != null && mCb.get() != null){
                    mCb.get().onLoaded();
                }
                OrderManager.getInstance().startCheckOrderStateTask();
            }
        });
    }

    public interface Callback{
        void onLoaded();
    }
}
