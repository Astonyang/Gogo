package com.xxx.gogo.model.order;

import java.util.ArrayList;

public class OrderDetailGoodsItemModel {
    private Callback mCb;
    private ArrayList<OrderDetailGoodsItem> mDatas;

    public OrderDetailGoodsItemModel(Callback callback){
        mDatas = new ArrayList<>();
        mCb = callback;
    }

    public void load(){
        OrderDetailGoodsItem item = new OrderDetailGoodsItem();
        item.name = "mmmmm";
        item.count = 10;
        item.actualCount = 10;
        item.unitPrice = 99;
        item.totalPrice = 109;

        mDatas.add(item);

        mCb.onSuccess();
    }

    public int getCount(){
        return mDatas.size();
    }

    public OrderDetailGoodsItem getItem(int pos){
        return mDatas.get(pos);
    }

    public interface Callback{
        void onSuccess();
    }
}
