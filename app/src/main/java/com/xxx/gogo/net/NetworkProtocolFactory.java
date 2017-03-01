package com.xxx.gogo.net;

public class NetworkProtocolFactory {
    public static NetworkRequest.LoginRequest buildLoginRequest(String userName,
                                                                String password){
        NetworkRequest.LoginRequest request = new NetworkRequest.LoginRequest();
        request.userName = userName;
        request.password = password;

        return request;
    }

    public static NetworkRequest.RegisterRequest buildRegisterRequest(String userName,
                                                                      String password){
        NetworkRequest.RegisterRequest request = new NetworkRequest.RegisterRequest();
        request.userName = userName;
        request.password = password;

        return request;
    }
}
