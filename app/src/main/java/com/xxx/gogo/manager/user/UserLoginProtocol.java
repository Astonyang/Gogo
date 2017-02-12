package com.xxx.gogo.manager.user;

import com.google.gson.Gson;


public class UserLoginProtocol {
    public static UserLoginResponse parseLoginResponse(String response){
        Gson gson = new Gson();
        return gson.fromJson(response, UserLoginResponse.class);
    }

    public static class UserLoginResponse {//extends BaseResponse {
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
