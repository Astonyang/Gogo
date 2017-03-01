package com.xxx.gogo.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxx.gogo.net.gson_adapter.LoginResponseAdapter;
import com.xxx.gogo.net.gson_adapter.RegisterResponseAdapter;
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
