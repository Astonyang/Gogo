package com.xxx.gogo.model.user;

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
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoModel implements LowMemoryListener{
    private static final String USER_INFO = "user/user_info";

    private UserInfo mInfo;

    public static class InstanceHolder{
        static UserInfoModel infoModel = new UserInfoModel();
    }

    public static UserInfoModel getInstance(){
        return InstanceHolder.infoModel;
    }

    private UserInfoModel(){
    }

    public void saveInfo(UserInfo info){
        Preconditions.checkNotNull(info);

        mInfo = info;
        Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
        final String strJson = gson.toJson(info);

        Call<NetworkResponse.UserInfoCommitResponse> call = NetworkServiceFactory.getInstance()
                .getService().commitUserInfo(NetworkProtocolFactory.buildUserInfoCommitRequest());
        call.enqueue(new Callback<NetworkResponse.UserInfoCommitResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.UserInfoCommitResponse> call,
                                   Response<NetworkResponse.UserInfoCommitResponse> response) {
                if(response.isSuccessful() && response.body().isSuccessful()){
                    ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
                        @Override
                        public void run() {
                            final byte[] data = CryptoUtil.encrypt(strJson.getBytes());

                            FileManager.writeFile(USER_INFO, data);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.UserInfoCommitResponse> call, Throwable t) {

            }
        });
    }

    public void getInfo(final LoadCallback callback){
        if(mInfo != null){
            notifyLoadResult(callback, null);
            return;
        }

        Call<NetworkResponse.UserInfoLoadResponse> call = NetworkServiceFactory.getInstance().
                getService().loadUserInfo(NetworkProtocolFactory.buildUserInfoLoadRequest());
        call.enqueue(new Callback<NetworkResponse.UserInfoLoadResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.UserInfoLoadResponse> call,
                                   Response<NetworkResponse.UserInfoLoadResponse> response) {
                if(response.isSuccessful() && response.body().isSuccessful()){

                }else {
                    loadFromDisk(callback);
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.UserInfoLoadResponse> call, Throwable t) {
                loadFromDisk(callback);
            }
        });

    }

    private void loadFromDisk(final LoadCallback callback){
        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                byte[] data = CryptoUtil.deEncrypt(FileManager.readFile(USER_INFO));
                if(data == null){
                    notifyLoadResult(callback, null);
                    return;
                }
                Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
                UserInfo info = gson.fromJson(new String(data), UserInfo.class);

                notifyLoadResult(callback, info);
            }
        });
    }

    @Override
    public void onLowMemory() {
        mInfo = null;
    }

    private void notifyLoadResult(final LoadCallback callback, final UserInfo info){
        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                callback.onLoaded(info);
            }
        });
    }


    public static class UserInfo{
        public String name;
        public String addr;
        public String title;
    }

    public interface LoadCallback{
        void onLoaded(UserInfo info);
    }
}
