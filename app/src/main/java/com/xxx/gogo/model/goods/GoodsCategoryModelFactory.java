package com.xxx.gogo.model.goods;

public class GoodsCategoryModelFactory {
    public static GoodsCategoryModel createCategoryModel(
            BaseGoodsCategoryModel.Callback callback,
            String providerId){

        GoodsCategoryNetDataSource netDataSource = new GoodsCategoryNetDataSource();
        GoodsCategoryLocalDataSource localDataSource = new GoodsCategoryLocalDataSource(
                netDataSource, providerId, "");
        return new GoodsCategoryModel(localDataSource, callback);
    }

    public static GoodsSubCategoryModel createSubCategoryModel(
            BaseGoodsCategoryModel.Callback callback,
            String providerId,
            String parentCatId){

        GoodsSubCategoryNetDataSource netDataSource = new GoodsSubCategoryNetDataSource();
        GoodsSubCategoryLocalDataSource localDataSource = new GoodsSubCategoryLocalDataSource(
                netDataSource, providerId, parentCatId);
        return new GoodsSubCategoryModel(localDataSource, callback);
    }
}
