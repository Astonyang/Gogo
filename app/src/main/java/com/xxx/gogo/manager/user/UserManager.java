package com.xxx.gogo.manager.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.model.user.UserLoginInfo;
import com.xxx.gogo.setting.SettingModel;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.CryptoUtil;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.ThreadManager;

import java.io.File;

public class UserManager implements UserAgent.Callback{
    private static final String USER_INFO = "user/user_login";

    private String mUserDir;
    private String mUserId;

    private UserAgent mAgent;

    private volatile boolean mIsLogin;
    private volatile boolean mShouldStartMainActivity;

    private static class InstanceHolder{
        private static UserManager sInstance = new UserManager();
    }

    public static UserManager getInstance(){
        return InstanceHolder.sInstance;
    }

    public void init(String rootDir){
        mUserDir += rootDir;
    }

    private UserManager(){
        mUserDir = "";
        mUserId = "";

        mAgent = new UserAgent(this);
    }

    public String getUserId(){
        return mUserId;
    }

    public String getUserDir(){
        return mUserDir;
    }

    //// TODO: 17/2/10 should remove later
    public boolean shouldStartMainActivity(){
        return mShouldStartMainActivity;
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

    public void login(){
        if(mIsLogin){
            return;
        }
        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
                byte[] data = CryptoUtil.deEncrypt(FileManager.readGlobalFile(USER_INFO));
                UserLoginInfo userLoginInfo = null;
                if(data != null){
                    userLoginInfo = gson.fromJson(new String(data), UserLoginInfo.class);
                }

                final UserLoginInfo info = userLoginInfo;
                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        if(info != null){
                            login(info.userName, info.pwd);
                        }else {
                            onLoginFail();
                        }
                    }
                });
            }
        });
    }

    public void login(String userName, String password){
        if(mIsLogin){
            return;
        }
        mAgent.login(userName, password);
    }

    public void logout(){
        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                FileManager.deleteGlobalFile(USER_INFO);

                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        mIsLogin = false;

                        BusFactory.getBus().post(new UserEvent.UserLogout());
                    }
                });
            }
        });
    }

    public void cancelLogin(){
        mAgent.cancelLogin();
    }

    public void cancelRegister(){
        mAgent.cancelRegister();
    }

    public void cancelFindPwd(){
        mAgent.cancelFindPwd();
    }

    @Override
    public void onLoginFail() {
        mShouldStartMainActivity = true;

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
    public void onLoginSuccess(String userId) {
        mUserId = userId;
        mUserDir += File.separator;
        mUserDir += mUserId;
        SettingModel.getInstance().getString(SettingModel.KEY_USER_ID, mUserId);

        mIsLogin = true;
        BusFactory.getBus().post(new UserEvent.UserLoginSuccess());

        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
                UserLoginInfo userLoginInfo = new UserLoginInfo("12233939", "123qwe");

                byte[] data = CryptoUtil.encrypt(gson.toJson(userLoginInfo).getBytes());

                FileManager.writeGlobalFile(USER_INFO, data);
            }
        });

        mShouldStartMainActivity = true;
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
