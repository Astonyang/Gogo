package com.xxx.gogo.model.goods;

import java.io.File;

class GoodsCategoryLocalDataSource extends BaseGoodsCategoryLocalDataSource{
    private static final String CAT_FILE_NAME = "goods_cat";

    GoodsCategoryLocalDataSource(BaseGoodsCategoryNetDataSource dataSource,
                                 String providerId,
                                 String parentId) {
        super(dataSource, providerId, parentId);
    }

    @Override
    String makePath() {
        return mProviderId + File.separator + CAT_FILE_NAME;
    }
}
