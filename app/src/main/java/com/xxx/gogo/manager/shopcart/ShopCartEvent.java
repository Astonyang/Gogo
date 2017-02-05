package com.xxx.gogo.manager.shopcart;

public class ShopCartEvent {
    public static class ShopCartDataLoaded{

    }

    public static class ShopCartDataChanged{
        public static final int TYPE_ADD = 0;
        public static final int TYPE_DELETE = 1;
        public static final int TYPE_PRICE_CHANGED = 2;

        public int mChangedType;

        public ShopCartDataChanged(int type){
            mChangedType = type;
        }
    }
}
