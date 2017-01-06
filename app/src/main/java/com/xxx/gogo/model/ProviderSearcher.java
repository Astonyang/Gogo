package com.xxx.gogo.model;

import com.xxx.gogo.model.provider.ProviderItemInfo;
import com.xxx.gogo.utils.ThreadManager;

public class ProviderSearcher {
    private Callback mCb;

    public ProviderSearcher(Callback cb){
        mCb = cb;
    }

    public void load(String query){
        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                ProviderItemInfo info = new ProviderItemInfo();
                info.id = "1001";
                info.name = "Jack King";
                info.phone = "1312839927";
                mCb.onSuccess(info);
            }
        });
    }

    public interface Callback{
        void onSuccess(ProviderItemInfo info);
        void onFail();
    }
}
