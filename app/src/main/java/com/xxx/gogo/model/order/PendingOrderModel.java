package com.xxx.gogo.model.order;

import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PendingOrderModel implements LowMemoryListener{
    private ArrayList<OrderItemInfo> mPendingOrderList;

    private WeakReference<AllOrderModel.Callback> mCb;

    private static class InstanceHolder{
        private static PendingOrderModel sInstance = new PendingOrderModel();
    }

    public static PendingOrderModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private PendingOrderModel(){
        mPendingOrderList = new ArrayList<>();
    }

    public void setCallback(AllOrderModel.Callback callback){
        mCb = new WeakReference<>(callback);
    }

    @Override
    public void onLowMemory() {

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
        return mPendingOrderList.size();
    }

    public OrderItemInfo getItem(int pos){
        return mPendingOrderList.get(pos);
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

            mPendingOrderList.add(info);
        }
    }
}
