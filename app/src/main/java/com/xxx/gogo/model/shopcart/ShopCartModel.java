package com.xxx.gogo.model.shopcart;

import com.xxx.gogo.model.goods.GoodsItemInfo;

import java.util.LinkedList;

public class ShopCartModel {
    private LinkedList<GoodsItemInfo> mGoodsList;

    private static class InstanceHolder{
        private static ShopCartModel sInstance = new ShopCartModel();
    }

    public static ShopCartModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private ShopCartModel(){
        mGoodsList = new LinkedList<>();
    }

    public int getCount(){
        return mGoodsList.size();
    }
}
