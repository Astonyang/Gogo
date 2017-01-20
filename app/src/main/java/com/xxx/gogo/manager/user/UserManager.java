package com.xxx.gogo.manager.user;

import com.google.gson.Gson;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.model.user.UserLoginInfo;
import com.xxx.gogo.utils.CryptoUtil;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.ThreadManager;

public class UserManager implements UserAgent.Callback{
    private static final String USER_INFO = "user/user_login";

    private UserAgent mAgent;
    private boolean mIsLogin;

    private static class InstanceHolder{
        private static UserManager sInstance = new UserManager();
    }

    public static UserManager getInstance(){
        return InstanceHolder.sInstance;
    }

    private UserManager(){
        mAgent = new UserAgent(this);
    }

    public boolean isLogin(){
        return mIsLogin;
    }

    public void findPassword(String phoneNum) {
        mAgent.findPassword(phoneNum);
    }

    public void register(String userName, String password,
                         String checkSum, String invitationNum){
        mAgent.register(userName, password, checkSum, invitationNum);
    }

    public void login(String userName, String password){
        mAgent.login(userName, password);
    }

    @Override
    public void onLoginFail() {
        BusFactory.getBus().post(new UserEvent.UserLoginFail());
    }

    @Override
    public void onRegisterFail() {
        BusFactory.getBus().post(new UserEvent.UserRegisterFail());
    }

    @Override
    public void onFindPasswordFail() {
        BusFactory.getBus().post(new UserEvent.UserFindPasswordFail());
    }

    @Override
    public void onLoginSuccess() {
        mIsLogin = true;
        BusFactory.getBus().post(new UserEvent.UserLoginSuccess());

        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                UserLoginInfo userLoginInfo = new UserLoginInfo("12233939", "123qwe");

                byte[] data = CryptoUtil.encrypt(gson.toJson(userLoginInfo).getBytes());

                FileManager.writeFile(USER_INFO, data);
            }
        });
    }

    @Override
    public void onRegisterSuccess() {
        BusFactory.getBus().post(new UserEvent.UserRegisterSuccess());
    }

    @Override
    public void onFindPasswordSuccess() {
        BusFactory.getBus().post(new UserEvent.UserFindPasswordSuccess());
    }
}
