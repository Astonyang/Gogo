package com.xxx.gogo.model.order;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class OrderItemDetailModel {
    private ArrayList<OrderItemDetailInfo> mDatas;
    private OrderItemInfo mOrderInfo;

    public OrderItemDetailModel(OrderItemInfo info){
        mDatas = new ArrayList<>();
        mOrderInfo = info;
    }

    public void load(Callback cb){
        final WeakReference<Callback> callback = new WeakReference<>(cb);
    }

    public int getCount(){
        return mDatas.size();
    }

    public OrderItemDetailInfo getItem(int pos){
        return mDatas.get(pos);
    }

    public interface Callback{
        void onSuccess();
    }
}
