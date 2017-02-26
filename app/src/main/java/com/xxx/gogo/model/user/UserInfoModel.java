package com.xxx.gogo.model.user;

import com.google.gson.Gson;
import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.CryptoUtil;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.Preconditions;
import com.xxx.gogo.utils.ThreadManager;

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
        Gson gson = new Gson();
        final String strJson = gson.toJson(info);

        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                final byte[] data = CryptoUtil.encrypt(strJson.getBytes());

                FileManager.writeFile(USER_INFO, data);
            }
        });
    }

    public void getInfo(final LoadCallback callback){
        if(mInfo != null){
            notifyLoadResult(callback, null);
            return;
        }
        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                byte[] data = CryptoUtil.deEncrypt(FileManager.readFile(USER_INFO));
                if(data == null){
                    notifyLoadResult(callback, null);
                    return;
                }
                Gson gson = new Gson();
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
