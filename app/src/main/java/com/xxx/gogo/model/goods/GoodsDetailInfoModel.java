package com.xxx.gogo.model.goods;

public class GoodsDetailInfoModel {

    private GoodsItemInfo mInfo;

    private static class InstanceHolder{
        private static GoodsDetailInfoModel sInstance = new GoodsDetailInfoModel();
    }

    private GoodsDetailInfoModel(){

    }

    public static GoodsDetailInfoModel getInstance(){
        return InstanceHolder.sInstance;
    }

    public void setInfo(GoodsItemInfo info){
        mInfo = info;
    }

    public GoodsItemInfo getInfo(){
        return mInfo;
    }
}
