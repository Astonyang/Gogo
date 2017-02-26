package com.xxx.gogo.manager.order;

import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.order.OrderItemDetailInfo;
import com.xxx.gogo.model.order.OrderItemInfo;
import com.xxx.gogo.model.order.OrderModel;
import com.xxx.gogo.model.store_mgr.StoreInfoModel;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderManager {
    private static final long CHECK_STATE_INTERVAL = 1000*60*60;

    private OrderCommitAgent mAgent;
    private Runnable mTask;

    public static class InstanceHolder{
        private static OrderManager sInstance = new OrderManager();
    }

    public static OrderManager getInstance(){
        return InstanceHolder.sInstance;
    }

    private OrderManager(){
        mAgent = new OrderCommitAgent();
        mTask = new CheckStateTask();
    }

    public void init(){
        OrderModel.getInstance().load();
    }

    public void commitOrder(final Map<String, List<GoodsItemInfo>> goodsMap,
                            final CommitCallback cb){
        //ensure load order completed
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
                    @Override
                    public void run() {
                        final List<OrderItemInfo> orderList = new ArrayList<>(goodsMap.size());
                        final List<List<OrderItemDetailInfo>> detailList = new ArrayList<>(goodsMap.size());

                        for(Map.Entry<String, List<GoodsItemInfo>> entry : goodsMap.entrySet()){
                            List<OrderItemDetailInfo> details = new ArrayList<>();
                            List<GoodsItemInfo> goodsList = entry.getValue();

                            double totalPrice = 0;
                            for (GoodsItemInfo goodsItemInfo : goodsList){
                                OrderItemDetailInfo detailInfo = new OrderItemDetailInfo();
                                detailInfo.name = goodsItemInfo.name;
                                detailInfo.unitPrice = goodsItemInfo.price;
                                detailInfo.totalPrice = goodsItemInfo.price * goodsItemInfo.count;
                                detailInfo.actualCount = goodsItemInfo.count;
                                detailInfo.count = goodsItemInfo.count;

                                totalPrice += detailInfo.totalPrice;

                                details.add(detailInfo);
                            }
                            final OrderItemInfo info = new OrderItemInfo();
                            orderList.add(info);
                            detailList.add(details);

                            info.id = OrderItemInfo.generateOrderId();
                            info.state = OrderItemInfo.STATE_CREATED;
                            info.goodsNum = goodsList.size();
                            info.price = totalPrice;
                            info.startTime = System.currentTimeMillis();

                            StoreInfoModel.getInstance().getInfo(new StoreInfoModel.Callback() {
                                @Override
                                public void onLoaded(StoreInfoModel.StoreInfo storeInfo) {
                                    if(storeInfo != null){
                                        info.storeName = storeInfo.name;
                                    }
                                    mAgent.commit(orderList, detailList, new OrderCommitAgent.Callback(){
                                        @Override
                                        public void onFail() {
                                            cb.onCommitFail();
                                        }

                                        @Override
                                        public void onSuccess() {
                                            OrderModel.getInstance().add(orderList, detailList);
                                            cb.onCommitSuccess();
                                        }
                                    });
                                }
                            });

                        }
                    }
                });
            }
        });
    }

    public void cancelOrder(final String orderId, final CancelCallback callback){
        mAgent.cancel(orderId, new OrderCommitAgent.Callback(){
            @Override
            public void onFail() {
                callback.onCancelFail();
            }

            @Override
            public void onSuccess() {
                OrderModel.getInstance().modifyState(orderId, OrderItemInfo.STATE_CANCELED);
                callback.onCancelSuccess();
            }
        });
    }

    public void startCheckOrderStateTask(){
        ThreadManager.postTask(ThreadManager.TYPE_WORKER, mTask);
    }

    public void checkOrderState(){
        mAgent.queryOrderState(new OrderCommitAgent.QueryStateCallback() {
            @Override
            public void onState(Map<String, Integer> stateMap) {
                if(stateMap == null){
                    BusFactory.getBus().post(new OrderEvent.CheckOrderStateComplete(false));
                    return;
                }
                String orderId = stateMap.keySet().iterator().next();
                int state = stateMap.get(orderId);
                OrderModel.getInstance().modifyState(orderId, state);
            }
        });
    }

    private class CheckStateTask implements Runnable{
        @Override
        public void run() {
            mAgent.queryOrderState(new OrderCommitAgent.QueryStateCallback() {
                @Override
                public void onState(Map<String, Integer> stateMap) {
                    if(stateMap == null){
                        return;
                    }
                    if(OrderModel.getInstance().hasPendingOrders()){
                        ThreadManager.postTask(ThreadManager.TYPE_UI, mTask, CHECK_STATE_INTERVAL);
                    }
                    String orderId = stateMap.keySet().iterator().next();
                    int state = stateMap.get(orderId);
                    OrderModel.getInstance().modifyState(orderId, state);
                }
            });
        }
    }

    public interface CommitCallback{
        void onCommitSuccess();
        void onCommitFail();
    }

    public interface CancelCallback{
        void onCancelSuccess();
        void onCancelFail();
    }
}
