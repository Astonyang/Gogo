package com.xxx.gogo.net;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NetworkService {

    String PATH_PREFIX = "/purchase/api/v1/mobile/customer";

//    @POST("order.php")
//    Call<List<Repo>> commitOrder(@Body OrderItemInfo);

    @POST(PATH_PREFIX + "/login.php")
    Call<NetworkResponse.LoginResponse> login(@Body NetworkRequest.LoginRequest request);

    @POST(PATH_PREFIX + "/regist.php")
    Call<NetworkResponse.RegisterResponse> register(@Body NetworkRequest.RegisterRequest request);
}
