package com.xxx.gogo.model.often_buy;

import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.net.NetworkProtocolFactory;
import com.xxx.gogo.net.NetworkResponse;
import com.xxx.gogo.net.NetworkServiceFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OftenBuyNetDataSource {

    void load(final OftenBuyLocalDataSource callback){
        Call<NetworkResponse.OftenbuyLoadResponse> call = NetworkServiceFactory.getInstance()
                .getService().loadOftenBuy(NetworkProtocolFactory.buildOftenBuyLoadRequest());
        call.enqueue(new Callback<NetworkResponse.OftenbuyLoadResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.OftenbuyLoadResponse> call,
                                   Response<NetworkResponse.OftenbuyLoadResponse> response) {

                if(response.isSuccessful() && response.body().isSuccessful()){
                    callback.onDataReady(null);
                }else {
                    callback.onDataReady(null);
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.OftenbuyLoadResponse> call, Throwable t) {
                callback.onDataReady(null);
            }
        });
    }

    void commit(List<GoodsItemInfo> goodsItemInfoList){
        Call<NetworkResponse.OftenbuyCommitResponse> call = NetworkServiceFactory.getInstance()
                .getService().commitOftenBuy(NetworkProtocolFactory.buildOftenBuyCommitRequest());
        call.enqueue(new Callback<NetworkResponse.OftenbuyCommitResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.OftenbuyCommitResponse> call,
                                   Response<NetworkResponse.OftenbuyCommitResponse> response) {

            }

            @Override
            public void onFailure(Call<NetworkResponse.OftenbuyCommitResponse> call, Throwable t) {

            }
        });
    }
}
