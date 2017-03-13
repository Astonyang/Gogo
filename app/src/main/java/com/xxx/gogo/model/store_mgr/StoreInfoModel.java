package com.xxx.gogo.model.store_mgr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.net.NetworkProtocolFactory;
import com.xxx.gogo.net.NetworkResponse;
import com.xxx.gogo.net.NetworkServiceFactory;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.CryptoUtil;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.Preconditions;
import com.xxx.gogo.utils.ThreadManager;

import retrofit2.Call;
import retrofit2.Response;

public class StoreInfoModel implements LowMemoryListener{
    private static final String SHOP_INFO = "user/shop_info";

    private StoreInfo mInfo;

    public static class InstanceHolder{
        static StoreInfoModel infoModel = new StoreInfoModel();
    }

    public static StoreInfoModel getInstance(){
        return InstanceHolder.infoModel;
    }

    private StoreInfoModel(){
    }

    public void save(StoreInfo info){
        Preconditions.checkNotNull(info);

        mInfo = info;
        Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
        final byte[] data = gson.toJson(mInfo).getBytes();

        Call<NetworkResponse.StoreInfoCommitResponse> call = NetworkServiceFactory.getInstance().
                getService().commitStoreInfo(NetworkProtocolFactory.buildStoreInfoCommitRequest());

        call.enqueue(new retrofit2.Callback<NetworkResponse.StoreInfoCommitResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.StoreInfoCommitResponse> call,
                                   Response<NetworkResponse.StoreInfoCommitResponse> response) {

                if(response.isSuccessful() && response.body().isSuccessful()){
                    ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
                        @Override
                        public void run() {
                            FileManager.writeFile(SHOP_INFO, CryptoUtil.encrypt(data));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.StoreInfoCommitResponse> call, Throwable t) {

            }
        });
    }

    public StoreInfo getInfo(){
        return mInfo;
    }

    public void getInfo(final Callback callback){
        if(mInfo != null){
            callback.onLoaded(mInfo);
            return;
        }
        Call<NetworkResponse.StoreInfoLoadResponse> call = NetworkServiceFactory.getInstance()
                .getService().loadStoreInfo(NetworkProtocolFactory.buildStoreInfoLoadRequest());
        call.enqueue(new retrofit2.Callback<NetworkResponse.StoreInfoLoadResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.StoreInfoLoadResponse> call,
                                   Response<NetworkResponse.StoreInfoLoadResponse> response) {

                if(response.isSuccessful() && response.body().isSuccessful()){

                }else {
                    loadFromDisk(callback);
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.StoreInfoLoadResponse> call, Throwable t) {
                loadFromDisk(callback);
            }
        });
    }

    private void loadFromDisk(final Callback callback){
        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                byte[] rawData = FileManager.readFile(SHOP_INFO);
                if(rawData != null && rawData.length != 0){
                    byte[] data = CryptoUtil.deEncrypt(rawData);
                    if(data == null){
                        notifyFail(callback);
                        return;
                    }
                    final String strJson = new String(data);

                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
                            mInfo = gson.fromJson(strJson, StoreInfo.class);
                            callback.onLoaded(mInfo);
                        }
                    });
                }else {
                    notifyFail(callback);
                }
            }
        });
    }

    private void notifyFail(final Callback callback){
        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                callback.onLoaded(null);
            }
        });
    }

    public static class StoreInfo{
        public String name;
        public String addr;
        public String owner;
        public String phone;
        public String startTime;
        public String endTime;
    }

    @Override
    public void onLowMemory() {
        mInfo = null;
    }

    public interface Callback{
        void onLoaded(StoreInfo storeInfo);
    }
}
