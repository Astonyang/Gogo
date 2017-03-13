package com.xxx.gogo.model.provider;

import com.xxx.gogo.net.NetworkProtocolFactory;
import com.xxx.gogo.net.NetworkResponse;
import com.xxx.gogo.net.NetworkServiceFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ProviderNetDataSource {
    private ProviderLocalDataSource mCb;

    ProviderNetDataSource(ProviderLocalDataSource callback){
        mCb = callback;
    }

    void addItem(ProviderItemInfo info){
        Call<NetworkResponse.ProviderAddResponse> call = NetworkServiceFactory.getInstance()
                .getService().addProvider(NetworkProtocolFactory.buildProviderAddRequest(info.id));
        call.enqueue(new Callback<NetworkResponse.ProviderAddResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.ProviderAddResponse> call,
                                   Response<NetworkResponse.ProviderAddResponse> response) {

            }

            @Override
            public void onFailure(Call<NetworkResponse.ProviderAddResponse> call, Throwable t) {

            }
        });
    }

    void deleteItem(ProviderItemInfo info){
        Call<NetworkResponse.ProviderRemoveResponse> call = NetworkServiceFactory.getInstance()
                .getService().removeProvider(NetworkProtocolFactory.buildProviderRemoveRequest(info.id));
        call.enqueue(new Callback<NetworkResponse.ProviderRemoveResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.ProviderRemoveResponse> call,
                                   Response<NetworkResponse.ProviderRemoveResponse> response) {

            }

            @Override
            public void onFailure(Call<NetworkResponse.ProviderRemoveResponse> call, Throwable t) {

            }
        });
    }

    void load(String contentVersion){
        Call<NetworkResponse.ProviderLoadResponse> call = NetworkServiceFactory.getInstance()
                .getService().loadProvider(NetworkProtocolFactory.buildProviderLoadRequest(contentVersion));
        call.enqueue(new Callback<NetworkResponse.ProviderLoadResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.ProviderLoadResponse> call,
                                   Response<NetworkResponse.ProviderLoadResponse> response) {
                if(mCb == null){
                    return;
                }
                if(response.isSuccessful() && response.body().isSuccessful()){
                    mCb.onDataReady(null);
                }else {
                    mCb.onDataReady(getTestData());
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.ProviderLoadResponse> call, Throwable t) {
                if(mCb != null){
                    mCb.onDataReady(null);
                }
            }
        });
    }

    private ArrayList<ProviderItemInfo> getTestData(){
        ArrayList<ProviderItemInfo> datas = new ArrayList<>();
        for (int i = 0; i < 20; ++i){
            ProviderItemInfo info = new ProviderItemInfo();
            info.id = String.valueOf(i);
            info.name = "Provider_" + String.valueOf(i);
            info.phone = String.valueOf(10000000 + i);
            info.addr = "Wall Street 123";
            info.url = "www.google.com";
            info.lat = 23.1;
            info.lng = 12.3;
            datas.add(info);
        }

        return datas;
    }
}
