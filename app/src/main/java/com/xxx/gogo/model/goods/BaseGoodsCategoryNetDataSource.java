package com.xxx.gogo.model.goods;

import com.xxx.gogo.net.NetworkProtocolFactory;
import com.xxx.gogo.net.NetworkResponse;
import com.xxx.gogo.net.NetworkServiceFactory;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract class BaseGoodsCategoryNetDataSource {
    String name[] = {"猪肉", "牛肉", "羊肉", "鸡肉", "鸭肉", "鱼肉", "牛蛙", "螃蟹", "蔬菜", "水果", "主食",
            "菌类", "调料", "饮品", "酒水", "C16", "C17", "C18", "C19", "C20", "C21"};

    void load(final GoodsCategoryLocalDataSource.Callback callback,
              final String providerId,
              final String parentCatID,
              boolean forceLoad){

        Call<NetworkResponse.GoodsCategoryLoadResponse> call = NetworkServiceFactory.getInstance()
                .getService().loadGoodsCategory(NetworkProtocolFactory.buildGoodsCategoryRequest());
        call.enqueue(new Callback<NetworkResponse.GoodsCategoryLoadResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.GoodsCategoryLoadResponse> call,
                                   Response<NetworkResponse.GoodsCategoryLoadResponse> response) {

                if(callback == null){
                    return;
                }
                if(response.isSuccessful() && response.body().isSuccessful()){
                    callback.onSuccess(null);
                }else {
                    callback.onFail(false);
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.GoodsCategoryLoadResponse> call, Throwable t) {
                if(callback != null){
                    callback.onFail(false);
                }
            }
        });

    }

    //// TODO: 17/3/2 for test
    private void test(final GoodsCategoryLocalDataSource.Callback callback){
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
