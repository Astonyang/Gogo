package com.xxx.gogo.model.goods;

public class GoodsCategoryModel {
    private LocalDataSoure mDataSource;

    String name[] = {"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "C11"};

    private static class InstanceHolder{
        private static GoodsCategoryModel sInstance = new GoodsCategoryModel();
    }

    public static GoodsCategoryModel getInstance(){
        return InstanceHolder.sInstance;
    }

    public int getCount(){
        return name.length;
    }

    public String getName(int pos){
        return name[pos];
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
