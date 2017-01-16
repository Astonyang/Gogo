package com.xxx.gogo.model.offen_buy;

import com.xxx.gogo.model.goods.GoodsItemInfo;

public class OffenBuyModel {
    private static class InstanceHolder{
        private static OffenBuyModel sInstance = new OffenBuyModel();
    }

    public static OffenBuyModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private OffenBuyModel(){
    }

    public int getCount(){
        return 20;
    }

    public GoodsItemInfo getGoodsItem(int pos){
        GoodsItemInfo itemInfo = new GoodsItemInfo();
        itemInfo.id = "000001";
        itemInfo.imgUrl = "";
        itemInfo.introduce = "Black meal";
        itemInfo.name = "Meal (kg)";
        itemInfo.price = "$100";
        itemInfo.providerId = "000001";

        return itemInfo;
    }
}
