package com.xxx.gogo.utils;

public interface Constants {
    //db
    int DATABASE_VERSION = 1;

    int START_SEARCH_PROVIDER_CODE = 0;
    int START_LOGIN_FROM_SHOPCART = 1;
    int START_LOGIN_FROM_FAVO = 2;
    int START_LOGIN_FROM_ME = 3;
    int START_ORDER_DETAIL_ACTIVITY = 4;
    int START_ORDER_LIST_ACTIVITY = 5;

    String KEY_EXIT_ACTIVITY = "exit_activity";
    String KEY_SWITCH_SHOP_CART = "switch_to_shop_cart";

    String KEY_PROVIDER_ID = "provider_id";
    String KEY_GOODS_CATEGORY_ID = "goods_category_id";
    String KEY_ORDER_TYPE = "order_type";
    String KEY_ORDER_POSITION = "order_position";
}
