package com.xxx.gogo.model.shopcart;

import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.order.OrderConfirmModel;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

public class ShopCartModel {
    private LinkedList<GoodsItemInfo> mGoodsList;
    private WeakReference<Callback> mCb;

    private static class InstanceHolder{
        private static ShopCartModel sInstance = new ShopCartModel();
    }

    public static ShopCartModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private ShopCartModel(){
        mGoodsList = new LinkedList<>();
    }

    public void load(){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        GoodsItemInfo itemInfo = new GoodsItemInfo();
                        itemInfo.id = "000001";
                        itemInfo.imgUrl = "";
                        itemInfo.introduce = "Black meal";
                        itemInfo.name = "Meal (kg)";
                        itemInfo.price = "$100";
                        itemInfo.providerId = "000001";

                        mGoodsList.add(itemInfo);

                        Callback callback = mCb.get();
                        if(callback != null){
                            callback.onSuccess();
                        }
                    }
                });
            }
        });
    }

    public void setCallback(Callback callback){
        mCb = new WeakReference<>(callback);
    }

    public int getCount(){
        return mGoodsList.isEmpty() ? 0 : 20;
        //return mGoodsList.size();
    }

    public GoodsItemInfo getGoodsItem(int pos){
        return mGoodsList.get(0);
    }

    public void clear(){
        mGoodsList.clear();
    }

    public OrderConfirmModel createOrderConfirmModel(){
        return new OrderConfirmModel();
    }

    public interface Callback{
        void onSuccess();
        void onFail();
        void onShopCartChanged();
    }
}
