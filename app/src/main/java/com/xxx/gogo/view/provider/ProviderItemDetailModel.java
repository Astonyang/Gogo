package com.xxx.gogo.view.provider;

import com.xxx.gogo.model.provider.ProviderItemInfo;

public class ProviderItemDetailModel {
    private ProviderItemInfo mItemInfo;

    private static class InstanceHolder{
        private static ProviderItemDetailModel sInstance = new ProviderItemDetailModel();
    }

    public static ProviderItemDetailModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private ProviderItemDetailModel(){

    }

    public void setProviderItem(ProviderItemInfo item){
        mItemInfo = item;
    }

    public ProviderItemInfo getItemInfo(){
        return mItemInfo;
    }
}
