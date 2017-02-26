package com.xxx.gogo.model.goods;

import com.xxx.gogo.setting.SettingModel;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;

class GoodsCategoryNetDataSource {
    //private static final long MIN_INTERVAL_LOADING_DATA = 1000*60*60;

    String name[] = {"猪肉", "牛肉", "羊肉", "鸡肉", "鸭肉", "鱼肉", "牛蛙", "螃蟹", "蔬菜", "水果", "主食",
            "菌类", "调料", "饮品", "酒水", "C16", "C17", "C18", "C19", "C20", "C21"};

    void load(final GoodsCategoryLocalDataSource.Callback callback,
              final String providerId,
              boolean forceLoad){

//        long lastTime = SettingModel.getInstance().getLong(
//                SettingModel.KEY_LAST_LOAD_CAT_FROM_SERVER_TIME, 0);
//
//        long currentTime = System.currentTimeMillis();
//
//        if(currentTime - lastTime < MIN_INTERVAL_LOADING_DATA && !forceLoad){
//            callback.onSuccess(null);
//            return;
//        }
//        SettingModel.getInstance().putLong(SettingModel.KEY_LAST_LOAD_CAT_FROM_SERVER_TIME, currentTime);

        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                List<GoodsCategoryItemInfo> infoList = new ArrayList<>();
                for (String item : name){
                    GoodsCategoryItemInfo itemInfo = new GoodsCategoryItemInfo();
                    itemInfo.id = infoList.size();
                    itemInfo.name = item;

                    infoList.add(itemInfo);
                }
                callback.onSuccess(infoList);
                //callback.onFail(false);
            }
        }, 1000);
    }
}
