package com.xxx.gogo.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxx.gogo.net.gson_adapter.response.GoodsCategoryLoadResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.GoodsLoadResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.GoodsSubCategoryLoadResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.LoginResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.OftenbufCommitResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.OftenbuyLoadResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.OrderCancelResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.OrderCommitResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.OrderModifyResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.OrderQueryStateResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.ProviderAddResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.ProviderLoadResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.ProviderRemoveResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.ProviderSearchResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.RegisterResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.StoreInfoCommitResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.StoreInfoLoadResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.UserInfoCommitResponseAdapter;
import com.xxx.gogo.net.gson_adapter.response.UserInfoLoadResponseAdapter;
import com.xxx.gogo.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkServiceFactory {
    //private String BASE_URL = "http://101.200.175.195:85/";
    private String BASE_URL = "http://haha.com/";

    private NetworkService mService;

    private static class InstanceHolder{
        private static NetworkServiceFactory sInstance = new NetworkServiceFactory();
    }

    public static NetworkServiceFactory getInstance(){
        return InstanceHolder.sInstance;
    }

    private NetworkServiceFactory(){
    }

    public void create(){
        Gson gson = new GsonBuilder()
                .setVersion(Constants.GSON_VERSION)
                .registerTypeAdapter(NetworkResponse.LoginResponse.class, new LoginResponseAdapter())
                .registerTypeAdapter(NetworkResponse.RegisterResponse.class, new RegisterResponseAdapter())
                .registerTypeAdapter(NetworkResponse.ProviderSearchResponse.class, new ProviderSearchResponseAdapter())
                .registerTypeAdapter(NetworkResponse.ProviderLoadResponse.class, new ProviderLoadResponseAdapter())
                .registerTypeAdapter(NetworkResponse.ProviderAddResponse.class, new ProviderAddResponseAdapter())
                .registerTypeAdapter(NetworkResponse.ProviderRemoveResponse.class, new ProviderRemoveResponseAdapter())
                .registerTypeAdapter(NetworkResponse.OftenbuyLoadResponse.class, new OftenbuyLoadResponseAdapter())
                .registerTypeAdapter(NetworkResponse.OftenbuyCommitResponse.class, new OftenbufCommitResponseAdapter())
                .registerTypeAdapter(NetworkResponse.GoodsCategoryLoadResponse.class, new GoodsCategoryLoadResponseAdapter())
                .registerTypeAdapter(NetworkResponse.GoodsSubCategoryLoadResponse.class, new GoodsSubCategoryLoadResponseAdapter())
                .registerTypeAdapter(NetworkResponse.GoodsLoadResponse.class, new GoodsLoadResponseAdapter())
                .registerTypeAdapter(NetworkResponse.OrderCommitResponse.class, new OrderCommitResponseAdapter())
                .registerTypeAdapter(NetworkResponse.OrderModifyResponse.class, new OrderModifyResponseAdapter())
                .registerTypeAdapter(NetworkResponse.OrderCancelResponse.class, new OrderCancelResponseAdapter())
                .registerTypeAdapter(NetworkResponse.OrderQueryStateResponse.class, new OrderQueryStateResponseAdapter())
                .registerTypeAdapter(NetworkResponse.UserInfoLoadResponse.class, new UserInfoLoadResponseAdapter())
                .registerTypeAdapter(NetworkResponse.UserInfoCommitResponse.class, new UserInfoCommitResponseAdapter())
                .registerTypeAdapter(NetworkResponse.StoreInfoCommitResponse.class, new StoreInfoCommitResponseAdapter())
                .registerTypeAdapter(NetworkResponse.StoreInfoLoadResponse.class, new StoreInfoLoadResponseAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mService = retrofit.create(NetworkService.class);
    }

    public NetworkService getService(){
        return mService;
    }
}
