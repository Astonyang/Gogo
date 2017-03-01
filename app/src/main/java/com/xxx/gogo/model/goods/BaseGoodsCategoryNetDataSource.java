package com.xxx.gogo.model.goods;

import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;

abstract class BaseGoodsCategoryNetDataSource {
    String name[] = {"猪肉", "牛肉", "羊肉", "鸡肉", "鸭肉", "鱼肉", "牛蛙", "螃蟹", "蔬菜", "水果", "主食",
            "菌类", "调料", "饮品", "酒水", "C16", "C17", "C18", "C19", "C20", "C21"};

    void load(final GoodsCategoryLocalDataSource.Callback callback,
              final String providerId,
              final String parentCatID,
              boolean forceLoad){

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
