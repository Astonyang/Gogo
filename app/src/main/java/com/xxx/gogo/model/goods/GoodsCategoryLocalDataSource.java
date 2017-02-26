package com.xxx.gogo.model.goods;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.ThreadManager;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

class GoodsCategoryLocalDataSource {
    private static final String CAT_FILE_NAME = "goods_cat";

    private GoodsCategoryNetDataSource mDataSource;
    private WeakReference<Callback> mCb;
    private String mProviderId;

    GoodsCategoryLocalDataSource(Callback callback, String providerId){
        mDataSource = new GoodsCategoryNetDataSource();
        mCb = new WeakReference<>(callback);
        mProviderId = providerId;
    }

    public void load(){
        boolean forceLoad = !FileManager.isExistGlobalFile(makePath());

        mDataSource.load(new Callback() {
            @Override
            public void onSuccess(final List<GoodsCategoryItemInfo> infoList) {
                ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
                    @Override
                    public void run() {
                        String path = makePath();

                        final List<GoodsCategoryItemInfo> infos;
                        Gson gson = new Gson();
                        if(infoList == null || infoList.isEmpty()){
                            byte[] data = FileManager.readGlobalFile(path);
                            if(data == null || data.length == 0){
                                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                                    @Override
                                    public void run() {
                                        if(mCb != null && mCb.get() != null){
                                            mCb.get().onFail(false);
                                        }
                                    }
                                });
                                return;
                            }
                            String str = new String(data);
                            infos = gson.fromJson(str,
                                    new TypeToken<List<GoodsCategoryItemInfo>>(){}.getType());
                        }else {
                            infos = infoList;
                            String jsonStr = gson.toJson(infoList,
                                    new TypeToken<List<GoodsCategoryItemInfo>>(){}.getType());
                            FileManager.writeGlobalFile(path, jsonStr.getBytes());
                        }

                        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                            @Override
                            public void run() {
                                if(mCb != null && mCb.get() != null){
                                    mCb.get().onSuccess(infos);
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onFail(boolean fromLocal) {
                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        if(mCb != null && mCb.get() != null){
                            mCb.get().onFail(false);
                        }
                    }
                });
            }
        }, mProviderId, forceLoad);
    }

    private String makePath(){
        return Constants.GOODS_DATA_ROOT_DIR + File.separator +
                mProviderId + File.separator + CAT_FILE_NAME;
    }

    interface Callback{
        void onSuccess(List<GoodsCategoryItemInfo> infoList);
        void onFail(boolean fromLocal);
    }
}
