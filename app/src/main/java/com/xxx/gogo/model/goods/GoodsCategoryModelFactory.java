package com.xxx.gogo.model.goods;

import com.xxx.gogo.utils.Preconditions;
import com.xxx.gogo.utils.ThreadManager;
import com.xxx.gogo.view.provider.ProviderDetailActivity;

public class GoodsCategoryModelFactory {
    private static GoodsCategoryModel sModel;

    public static GoodsCategoryModel createModel(GoodsCategoryModel.Callback callback, String providerId){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        Preconditions.checkArgument(callback instanceof ProviderDetailActivity,
                "Must be created from ProviderDetailActivity");

        sModel = new GoodsCategoryModel(callback, providerId);

        return sModel;
    }

    public static GoodsCategoryModel getModel(){
        return sModel;
    }
}
