package com.xxx.gogo.manager.order;

import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.order.OrderItemDetailInfo;
import com.xxx.gogo.model.order.OrderItemInfo;
import com.xxx.gogo.model.order.OrderModel;
import com.xxx.gogo.model.store_mgr.StoreInfoModel;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderManager implements OrderModel.Callback{

    private OrderCommitAgent mAgent;

    public static class InstanceHolder{
        private static OrderManager sInstance = new OrderManager();
    }

    public static OrderManager getInstance(){
        return InstanceHolder.sInstance;
    }

    private OrderManager(){
        mAgent = new OrderCommitAgent();
    }

    public void commitOrder(final HashMap<String, ArrayList<GoodsItemInfo>> goodsMap,
                            final CommitCallback cb){
        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                final ArrayList<OrderItemInfo> orderList = new ArrayList<>(goodsMap.size());
                final ArrayList<ArrayList<OrderItemDetailInfo>> detailList = new ArrayList<>(goodsMap.size());

                for(Map.Entry<String, ArrayList<GoodsItemInfo>> entry : goodsMap.entrySet()){
                    ArrayList<OrderItemDetailInfo> details = new ArrayList<>();
                    ArrayList<GoodsItemInfo> goodsList = entry.getValue();

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
                    OrderItemInfo info = new OrderItemInfo();
                    info.id = OrderItemInfo.generateOrderId();
                    info.state = OrderItemInfo.STATE_CREATED;
                    info.goodsNum = goodsList.size();
                    info.price = totalPrice;
                    StoreInfoModel.StoreInfo storeInfo = StoreInfoModel.getInstance().getInfo();
                    if(storeInfo != null){
                        info.storeName = storeInfo.name;
                    }
                    info.startTime = System.currentTimeMillis();

                    orderList.add(info);
                    detailList.add(details);
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

    public void cancelOrder(final String orderId, final CancelCallback callback){
        mAgent.cancel(orderId, new OrderCommitAgent.Callback(){
            @Override
            public void onFail() {
                callback.onCancelFail();
            }

            @Override
            public void onSuccess() {
                OrderModel.getInstance().delete(orderId);
                callback.onCancelSuccess();
            }
        });
    }

    @Override
    public void onOrderChanged() {

    }

    @Override
    public void onLoaded() {

    }

    @Override
    public void onAddFail() {

    }

    @Override
    public void onAddSuccess() {

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
