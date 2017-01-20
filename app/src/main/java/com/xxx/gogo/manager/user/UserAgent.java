package com.xxx.gogo.manager.user;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xxx.gogo.net.NetworkInterface;
import com.xxx.gogo.net.VolleyWrapper;
import com.xxx.gogo.utils.ThreadManager;

class UserAgent {
    private Callback mCb;

    UserAgent(Callback callback){
        mCb = callback;
    }

    void findPassword(String phoneNum){
        VolleyWrapper.getInstance().requestQueue().add(new StringRequest(
                NetworkInterface.USER_FIND_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCb.onFindPasswordSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        mCb.onFindPasswordSuccess();
                    }
                }, 2000);
                //mCb.onFindPasswordFail();
            }
        }));
    }

    void register(String userName, String password,
                         String checkSum, String invitationNum){
        VolleyWrapper.getInstance().requestQueue().add(new StringRequest(
                NetworkInterface.USER_REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCb.onRegisterSuccess();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        mCb.onRegisterSuccess();
                    }
                }, 2000);

                //mCb.onRegisterFail();
            }
        }));
    }

    void login(String userName, String password){
        VolleyWrapper.getInstance().requestQueue().add(new StringRequest(
                NetworkInterface.USER_LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCb.onLoginSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        mCb.onLoginSuccess();
                    }
                }, 2000);
                //mCb.onLoginFail();
            }
        }));
    }

    public interface Callback{
        void onFindPasswordSuccess();
        void onRegisterSuccess();
        void onLoginSuccess();
        void onFindPasswordFail();
        void onRegisterFail();
        void onLoginFail();
    }
}
