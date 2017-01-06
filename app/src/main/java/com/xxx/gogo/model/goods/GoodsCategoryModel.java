package com.xxx.gogo.model.goods;

public class GoodsCategoryModel {
    private LocalDataSoure mDataSource;

    String name[] = {"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "C11"};

    public int getCount(){
        return name.length;
    }

    public String getName(int pos){
        return name[pos];
    }
}
