package com.xxx.gogo.model.goods;

public class GoodsCategoryModel {
    private LocalDataSource mDataSource;

    String name[] = {"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "C11",
            "C21", "C22", "C23", "C24", "C25", "C26", "C27", "C28", "C29", "C20", "C21"};

    private static class InstanceHolder{
        private static GoodsCategoryModel sInstance = new GoodsCategoryModel();
    }

    public static GoodsCategoryModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private GoodsCategoryModel(){
    }

    public int getCategoryCount(String providerId){
        return name.length;
    }

    public String getCategoryName(String providerId, int position){
        return name[position];
    }

    public String getCategoryId(String providerId, int position){
        return String.valueOf(position);
    }
}
