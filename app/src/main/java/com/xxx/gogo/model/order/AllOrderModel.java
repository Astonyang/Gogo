package com.xxx.gogo.model.order;

import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AllOrderModel implements LowMemoryListener{
    private ArrayList<OrderItemInfo> mAllOrderList;

    private WeakReference<Callback> mCb;

    private OrderItemInfo mDetailItem;

    private static class InstanceHolder{
        private static AllOrderModel sInstance = new AllOrderModel();
    }

    public static AllOrderModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private AllOrderModel(){
        mAllOrderList = new ArrayList<>();
    }

    public void setCallback(Callback callback){
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
                        Callback callback = mCb.get();
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
        return mAllOrderList.size();
    }

    public OrderItemInfo getItem(int pos){
        return mAllOrderList.get(pos);
    }

    public OrderItemInfo getDetailItem(){
        return mDetailItem;
    }

    public void setDetailItem(OrderItemInfo info){
        mDetailItem = info;
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

            mAllOrderList.add(info);
        }
    }

    public interface Callback{
        void onSuccess();
        void onFail();
        void onOrderChanged();
    }
}
