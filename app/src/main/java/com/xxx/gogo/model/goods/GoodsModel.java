package com.xxx.gogo.model.goods;

public class GoodsModel {
    private static class InstanceHolder{
        private static GoodsModel sInstance = new GoodsModel();
    }

    public static GoodsModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private GoodsModel(){
    }

    public int getCount(String providerId, String categoryId){
        return 20;
    }

    public GoodsItemInfo getGoods(String providerId, String categoryId, int position){
        GoodsItemInfo itemInfo = new GoodsItemInfo();
        itemInfo.id = 100045;
        itemInfo.imgUrl = "";
        itemInfo.introduce = "Black meal";
        itemInfo.name = "Meal (kg)";
        itemInfo.price = 98.2;
        itemInfo.providerId = 10930;

        return itemInfo;
    }
}
