package com.xxx.gogo.model.goods;

import java.io.File;

class GoodsSubCategoryLocalDataSource extends BaseGoodsCategoryLocalDataSource{

    private static final String CAT_FILE_NAME = "goods_sub_cat";

    GoodsSubCategoryLocalDataSource(BaseGoodsCategoryNetDataSource dataSource,
                                    String providerId,
                                    String parentId) {
        super(dataSource, providerId, parentId);
    }

    @Override
    String makePath() {
        return mProviderId + File.separator + mParentCatId + File.separator + CAT_FILE_NAME;
    }
}
