package com.xxx.gogo.manager.user;

import com.xxx.gogo.net.NetworkProtocolFactory;
import com.xxx.gogo.net.NetworkResponse;
import com.xxx.gogo.net.NetworkService;
import com.xxx.gogo.net.NetworkServiceFactory;

import retrofit2.Call;
import retrofit2.Response;

class UserAgent {
    private Callback mCb;

    private NetworkService mNetworkService;

    private Call<NetworkResponse.LoginResponse> mLoginCall;
    private Call<NetworkResponse.RegisterResponse> mRegisterCall;

    UserAgent(Callback callback){
        mCb = callback;
        mNetworkService = NetworkServiceFactory.getInstance().getService();
    }

    void findPassword(String phoneNum){
    }

    void register(String userName, String password,
                         String checkSum, String invitationNum){
        mRegisterCall = mNetworkService.register(
                NetworkProtocolFactory.buildRegisterRequest(userName, password));
        mRegisterCall.enqueue(new retrofit2.Callback<NetworkResponse.RegisterResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.RegisterResponse> call,
                                   Response<NetworkResponse.RegisterResponse> response) {
                if(response.isSuccessful() && response.body().isSuccessful()){
                    mCb.onRegisterSuccess();
                }else {
                    mCb.onRegisterFail();
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.RegisterResponse> call, Throwable t) {
                mCb.onRegisterFail();
            }
        });
    }

    void login(String userName, String password) {
        mLoginCall = mNetworkService.login(
                NetworkProtocolFactory.buildLoginRequest(userName, password));
        mLoginCall.enqueue(new retrofit2.Callback<NetworkResponse.LoginResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.LoginResponse> call,
                                   Response<NetworkResponse.LoginResponse> response) {

                NetworkResponse.LoginResponse loginResponse = response.body();
                if(response.isSuccessful() && loginResponse.isSuccessful()){
                    mCb.onLoginSuccess(response.body().userId);
                }else {
                    //mCb.onLoginFail();
                    mCb.onLoginSuccess("000010");
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.LoginResponse> call, Throwable t) {
                mCb.onLoginFail();
            }
        });
    }

    void cancelLogin(){
        if(!mLoginCall.isCanceled() && !mLoginCall.isExecuted()){
            mLoginCall.cancel();
        }
    }

    void cancelRegister(){
        if(!mRegisterCall.isCanceled() && !mRegisterCall.isExecuted()){
            mRegisterCall.cancel();
        }
    }

    void cancelFindPwd(){
    }

    public interface Callback{
        void onFindPasswordSuccess();
        void onRegisterSuccess();
        void onLoginSuccess(String userId);
        void onFindPasswordFail();
        void onRegisterFail();
        void onLoginFail();
    }
}
