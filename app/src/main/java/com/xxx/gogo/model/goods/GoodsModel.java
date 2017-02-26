package com.xxx.gogo.model.goods;

import java.io.File;

public class GoodsModel {
    private static final String GOODS_DIR = "goods_dir";

    private String mProviderId;
    private String mCategoryId;

    private int mCurrentPage;

    public GoodsModel(String providerId, String categoryId){
        mProviderId = providerId;
        mCategoryId = categoryId;
    }

    public void loadNext(Callback callback){

    }

    public int getCount(){
        return 20;
    }

    public GoodsItemInfo getGoods(int position){
        GoodsItemInfo itemInfo = new GoodsItemInfo();
        itemInfo.id = GID[position % GID.length];
        itemInfo.imgUrl = "";
        itemInfo.introduce = "Black meal";
        itemInfo.name = "Meal (kg)";
        itemInfo.price = 98.2;
        itemInfo.providerId = Long.valueOf(mProviderId);

        return itemInfo;
    }

    private String makePath(){
        return mProviderId + File.separator + GOODS_DIR +
                File.separator + mCategoryId + File.separator + mCurrentPage;
    }

    int GID[] = {100001, 100002, 100003, 100004, 100005, 100006, 100007, 100008, 100009, 1100000};

    interface Callback{
        void onSuccess();
        void onFail();
    }
}
