package com.xxx.gogo.manager.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxx.gogo.utils.Constants;


@Deprecated
public class UserLoginProtocol {
    public static UserLoginResponse parseLoginResponse(String response){
        Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
        return gson.fromJson(response, UserLoginResponse.class);
    }

    public static class UserLoginResponse {//extends NetworkResponse {
        public String key;
        public ResultData data;

        public static class ResultData{
            public int result;
            public Data data;

            public static class Data{
                String customer_id;
            }
        }
    }
}
