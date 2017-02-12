package com.xxx.gogo.model.order;

import com.xxx.gogo.model.BaseModel;
import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class OrderModel  extends BaseModel
        implements LowMemoryListener, OrderLocalDataSource.Callback {
    public static final int TYPE_ALL = 0;
    public static final int TYPE_PENDING = 1;
    public static final int TYPE_COMPLETED = 2;

    private ArrayList<OrderItemInfo> mAllOrderList;
    private ArrayList<Integer> mPendingOrderList;
    private ArrayList<Integer> mCompletedOrderList;

    private OrderLocalDataSource mDataSource;
    private WeakReference<Callback> mCb;

    public static class InstanceHolder{
        private static OrderModel sInstance = new OrderModel();
    }

    public static OrderModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private OrderModel(){
        mAllOrderList = new ArrayList<>();
        mPendingOrderList = new ArrayList<>();
        mCompletedOrderList = new ArrayList<>();
        mDataSource = new OrderLocalDataSource(this);
    }

    public void setCallback(Callback cb){
        mCb = new WeakReference<>(cb);
    }

    public void add(ArrayList<OrderItemInfo> orderList,
                    ArrayList<ArrayList<OrderItemDetailInfo>> detailList){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        //// TODO: 17/2/11 处理是否追加到已有的订单中
        int preSize = mAllOrderList.size();
        mAllOrderList.addAll(orderList);
        int currSize = mAllOrderList.size();
        for (int i = preSize; i < currSize; ++i){
            mPendingOrderList.add(preSize);
        }
        mDataSource.add(orderList, detailList);
    }

    public void delete(String orderId){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        mDataSource.delete(orderId);
    }

    public void modifyState(String orderId, int state){

    }

    public void load(){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mState == STATE_INIT){
            mDataSource.load();
        }
    }

    public int getCount(int type){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

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

    public OrderItemInfo getItem(int type, int position){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        switch (type){
            case TYPE_ALL:
                return mAllOrderList.get(position);
            case TYPE_COMPLETED:
                return mAllOrderList.get(mCompletedOrderList.get(position));
            case TYPE_PENDING:
                return mAllOrderList.get(mPendingOrderList.get(position));
            default:
                return null;
        }
    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onCanceled(String orderId) {

    }

    @Override
    public void onLoaded(List<OrderItemInfo> orderItemInfoList) {
        ThreadManager.currentlyOn(ThreadManager.TYPE_DB);


        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                mState = STATE_LOADED;
                if(mCb != null && mCb.get() != null){
                    mCb.get().onLoaded();
                }
            }
        });
    }

    public interface Callback{
        void onAddSuccess();
        void onAddFail();
        void onLoaded();
        void onOrderChanged();
    }
}
