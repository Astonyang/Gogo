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
        itemInfo.id = GID[position % GID.length];
        itemInfo.imgUrl = "";
        itemInfo.introduce = "Black meal";
        itemInfo.name = "Meal (kg)";
        itemInfo.price = 98.2;
        itemInfo.providerId = Long.valueOf(providerId);

        return itemInfo;
    }

    int GID[] = {100001, 100002, 100003, 100004, 100005, 100006, 100007, 100008, 100009, 1100000};
}
