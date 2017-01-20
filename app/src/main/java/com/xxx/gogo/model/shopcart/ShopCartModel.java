package com.xxx.gogo.model.shopcart;

import android.database.sqlite.SQLiteOpenHelper;

import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.shopcart.ShopCartEvent;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.order.OrderConfirmModel;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopCartModel {
    private HashMap<String, GoodsItemInfo> mGoodsMap;
    private ArrayList<GoodsItemInfo> mGoodsList;
    private ShopCartDataSource mDataSource;

    private double mTotalPrice;

    private static class InstanceHolder{
        private static ShopCartModel sInstance = new ShopCartModel();
    }

    public static ShopCartModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private ShopCartModel(){
        mDataSource = new ShopCartDataSource(this);
    }

    public void setDbHelper(SQLiteOpenHelper dbHelper){
        mDataSource.setDbHelper(dbHelper);
    }

    public boolean contains(String id){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        return mGoodsMap.containsKey(id);
    }

    public void load(){
        mDataSource.load();
    }

    public void save(){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mGoodsList != null && !mGoodsList.isEmpty()){
            mDataSource.add(mGoodsList);
        }
    }

    public void deleteItem(final GoodsItemInfo info){
        if(mGoodsList == null){
            ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
                @Override
                public void run() {
                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            deleteInternal(info);
                        }
                    });
                }
            });
        }else {
            deleteInternal(info);
        }
    }

    public void addItem(final GoodsItemInfo info){
        /**
         * just in case that onDataReady was not called before delete or add called
         * we post "add" to db thread to queue after "load" operation
         */
        if(mGoodsList == null){
            ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
                @Override
                public void run() {
                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            addInternal(info);
                        }
                    });
                }
            });
        }else {
            addInternal(info);
        }
    }

    public void remove(int pos){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        GoodsItemInfo info = mGoodsList.get(pos);
        remove(info);
        mTotalPrice -= info.count * info.price;
    }

    public void remove(GoodsItemInfo info){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        mGoodsList.remove(info);
        mGoodsMap.remove(info.generateId());

        mTotalPrice -= info.count * info.price;

        BusFactory.getBus().post(new ShopCartEvent.ShopCartDataChanged(
                ShopCartEvent.ShopCartDataChanged.TYPE_DELETE));
    }

    private void addInternal(GoodsItemInfo info){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        String id = info.generateId();
        if(!mGoodsMap.containsKey(id)){
            info.count = 1;
            mGoodsList.add(info);
            mGoodsMap.put(info.generateId(), info);

            mTotalPrice += info.price;
        }

        BusFactory.getBus().post(new ShopCartEvent.ShopCartDataChanged(
                ShopCartEvent.ShopCartDataChanged.TYPE_ADD));
    }

    private void deleteInternal(GoodsItemInfo info){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(info.count == 0){
            remove(info);
        }else {
            info.count -= 1;
            if(info.count == 0){
                remove(info);
            }else {
                mTotalPrice -= info.price;
            }
        }

        BusFactory.getBus().post(new ShopCartEvent.ShopCartDataChanged(
                ShopCartEvent.ShopCartDataChanged.TYPE_DELETE));
    }

    public int getCount(){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        return (mGoodsList == null || mGoodsList.isEmpty()) ? 0 : mGoodsList.size();
    }

    public GoodsItemInfo getGoodsItem(int pos){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        return mGoodsList.get(pos);
    }

    public double getTotalPrice(){
        return mTotalPrice;
    }

    public void modifyTotalPrice(double oldValue, double newValue){
        mTotalPrice -= oldValue;
        mTotalPrice += newValue;

        BusFactory.getBus().post(new ShopCartEvent.ShopCartDataChanged(
                ShopCartEvent.ShopCartDataChanged.TYPE_COUNT_CHANGED));
    }

    public void clear(){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mGoodsList != null){
            mGoodsList.clear();
        }
        if(mGoodsMap != null){
            mGoodsMap.clear();
        }
        mTotalPrice = 0.0;
        mDataSource.clear();

        BusFactory.getBus().post(new ShopCartEvent.ShopCartDataChanged(
                ShopCartEvent.ShopCartDataChanged.TYPE_DELETE));
    }

    void onDataReady(ArrayList<GoodsItemInfo> list,
                     HashMap<String, GoodsItemInfo> goodsMap, double totalPrice){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        mTotalPrice = totalPrice;
        mGoodsMap = goodsMap;
        mGoodsList = list;
        BusFactory.getBus().post(new ShopCartEvent.ShopCartDataLoaded());
    }

    public OrderConfirmModel createOrderConfirmModel(){
        return new OrderConfirmModel();
    }
}
