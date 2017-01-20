package com.xxx.gogo.model.goods;

public class GoodsCategoryModel {
    private LocalDataSoure mDataSource;

    String name[] = {"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "C11",
            "C21", "C22", "C23", "C24", "C25", "C26", "C27", "C28", "C29", "C20", "C21"};

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
        itemInfo.id = 100045;
        itemInfo.imgUrl = "";
        itemInfo.introduce = "Black meal";
        itemInfo.name = "Meal (kg)";
        itemInfo.price = 98.2;
        itemInfo.providerId = 10930;

        return itemInfo;
    }
}
