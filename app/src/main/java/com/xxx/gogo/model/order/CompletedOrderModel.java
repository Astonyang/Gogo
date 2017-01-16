package com.xxx.gogo.model.order;

import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CompletedOrderModel implements LowMemoryListener{
    private ArrayList<OrderItemInfo> mCompletedOrderList;

    private WeakReference<AllOrderModel.Callback> mCb;

    private static class InstanceHolder{
        private static CompletedOrderModel sInstance = new CompletedOrderModel();
    }

    public static CompletedOrderModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private CompletedOrderModel(){
        mCompletedOrderList = new ArrayList<>();
    }

    public void setCallback(AllOrderModel.Callback callback){
        mCb = new WeakReference<>(callback);
    }

    public void load(){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {

                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        makeTestData();
                        AllOrderModel.Callback callback = mCb.get();
                        if(callback != null){
                            callback.onSuccess();
                        }
                    }
                });
            }
        });
    }

    public void clearCache(){

    }

    public int getCount(){
        return mCompletedOrderList.size();
    }

    public OrderItemInfo getItem(int pos){
        return mCompletedOrderList.get(pos);
    }

    @Override
    public void onLowMemory() {

    }

    void makeTestData(){
        for (int i = 0; i < 10; ++i){
            OrderItemInfo info = new OrderItemInfo();
            info.id = OrderItemInfo.generateOrderId();
            info.state = 0;
            info.goodsNum = "2";
            info.price = "99";
            info.shopName = "MMM kkkd d";
            info.startTime = String.valueOf(System.currentTimeMillis());

            mCompletedOrderList.add(info);
        }
    }
}
