package com.xxx.gogo.model.user;

import com.google.gson.Gson;
import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.CryptoUtil;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.Preconditions;

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
        FileManager.writeFile(USER_INFO, CryptoUtil.encrypt(gson.toJson(info).getBytes()));
    }

    public UserInfo getInfo(){
        if(mInfo != null){
            return mInfo;
        }
        byte[] data = CryptoUtil.deEncrypt(FileManager.readFile(USER_INFO));
        if(data == null){
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(new String(data), UserInfo.class);
    }

    @Override
    public void onLowMemory() {
        mInfo = null;
    }

    public static class UserInfo{
        public String name;
        public String addr;
        public String title;
    }
}
