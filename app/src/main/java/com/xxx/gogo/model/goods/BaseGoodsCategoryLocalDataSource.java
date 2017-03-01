package com.xxx.gogo.model.goods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.ThreadManager;

import java.util.List;

abstract class BaseGoodsCategoryLocalDataSource {
    private BaseGoodsCategoryNetDataSource mDataSource;
    String mProviderId;
    String mParentCatId;

    BaseGoodsCategoryLocalDataSource(BaseGoodsCategoryNetDataSource dataSource,
                                     String providerId,
                                     String parentId){

        mDataSource = dataSource;
        mProviderId = providerId;
        mParentCatId = parentId;
    }

    public void load(final Callback callback){
        boolean forceLoad = !FileManager.isExistGlobalFile(makePath());

        mDataSource.load(new GoodsCategoryLocalDataSource.Callback() {
            @Override
            public void onSuccess(final List<GoodsCategoryItemInfo> infoList) {
                ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
                    @Override
                    public void run() {
                        String path = makePath();

                        final List<GoodsCategoryItemInfo> infos;
                        Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
                        if(infoList == null || infoList.isEmpty()){
                            byte[] data = FileManager.readGlobalFile(path);
                            if(data == null || data.length == 0){
                                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFail(false);
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
                                callback.onSuccess(infos);
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
                        callback.onFail(false);
                    }
                });
            }
        }, mProviderId, mParentCatId, forceLoad);
    }

    abstract String makePath();

    public interface Callback{
        void onSuccess(List<GoodsCategoryItemInfo> infoList);
        void onFail(boolean fromLocal);
    }
}
