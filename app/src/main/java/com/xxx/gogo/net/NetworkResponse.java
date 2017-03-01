package com.xxx.gogo.net;

import com.google.gson.annotations.SerializedName;

public class NetworkResponse {

    private static final int ERROR_OK = 1;

    public static class BaseResponse{
        public String key;
        public int result;

        public boolean isSuccessful(){
            return result == ERROR_OK;
        }
    }

    public static class LoginResponse extends BaseResponse{
        @SerializedName("customer_id")
        public String userId;
    }

    public static class RegisterResponse extends BaseResponse{
        @SerializedName("customer_id")
        public String userId;
    }
}
