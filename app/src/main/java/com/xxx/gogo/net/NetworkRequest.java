package com.xxx.gogo.net;

import com.xxx.gogo.BuildConfig;

class NetworkRequest {

    static class BaseRequest{
        public String clientVersion = BuildConfig.VERSION_NAME;
        public String clientPlatform = "3";
    }

    static class LoginRequest extends BaseRequest{
        String userName;
        String password;
    }

    static class RegisterRequest extends BaseRequest{
        String userName;
        String password;
    }

    static class ProviderSearchRequest extends BaseRequest{

    }
}
