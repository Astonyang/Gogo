package com.xxx.gogo.net;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NetworkService {

    String PATH_PREFIX = "/purchase/api/v1/mobile/customer";

    @POST(PATH_PREFIX + "/login.php")
    Call<NetworkResponse.LoginResponse> login(@Body NetworkRequest.LoginRequest request);

    @POST(PATH_PREFIX + "/regist.php")
    Call<NetworkResponse.RegisterResponse> register(@Body NetworkRequest.RegisterRequest request);

    @POST(PATH_PREFIX + "/searchprovider.php")
    Call<NetworkResponse.ProviderSearchResponse> searchProvider(@Body NetworkRequest.ProviderSearchRequest request);

    @POST(PATH_PREFIX + "/getproviderlist.php")
    Call<NetworkResponse.ProviderLoadResponse> loadProvider(@Body NetworkRequest.ProviderLoadRequest request);

    @POST(PATH_PREFIX + "/manageprovider.php")
    Call<NetworkResponse.ProviderAddResponse> addProvider(@Body NetworkRequest.ProviderAddRequest request);

    @POST(PATH_PREFIX + "/manageprovider.php")
    Call<NetworkResponse.ProviderRemoveResponse> removeProvider(@Body NetworkRequest.ProviderRemoveRequest request);

    @POST(PATH_PREFIX + "/getoftenbuy.php")
    Call<NetworkResponse.OftenbuyLoadResponse> loadOftenBuy(@Body NetworkRequest.OftenbuyLoadRequest request);

    @POST(PATH_PREFIX + "/saveoftenbuy.php")
    Call<NetworkResponse.OftenbuyCommitResponse> commitOftenBuy(@Body NetworkRequest.OftenbufCommitRequest request);

    @POST(PATH_PREFIX + "/getcategorylist.php")
    Call<NetworkResponse.GoodsCategoryLoadResponse> loadGoodsCategory(@Body NetworkRequest.GoodsCategoryLoadRequest request);

//    @POST(PATH_PREFIX + "/")
//    Call<NetworkResponse.GoodsSubCategoryLoadResponse> loadSubGoodsCategory(@Body NetworkRequest.GoodsSubCategoryLoadRequest request);

    @POST(PATH_PREFIX + "/getgoodslist.php")
    Call<NetworkResponse.GoodsLoadResponse> loadGoods(@Body NetworkRequest.GoodsLoadRequest request);

    @POST(PATH_PREFIX + "/order.php")
    Call<NetworkResponse.OrderCommitResponse> commitOrder(@Body NetworkRequest.OrderCommitRequest request);

    @POST(PATH_PREFIX + "/deleteorder.php")
    Call<NetworkResponse.OrderCancelResponse> cancelOrder(@Body NetworkRequest.OrderCancelRequest request);

    @POST(PATH_PREFIX + "/modifyorder.php")
    Call<NetworkResponse.OrderModifyResponse> modifyOrder(@Body NetworkRequest.OrderModifyRequest request);

    @POST(PATH_PREFIX + "/")
    Call<NetworkResponse.OrderLoadResponse> loadOrders(@Body NetworkRequest.OrderLoadRequest request);

    @POST(PATH_PREFIX + "/purchase/api/v1/mobile/getorderstatus.php")
    Call<NetworkResponse.OrderQueryStateResponse> queryOrderState(@Body NetworkRequest.OrderQueryStateRequest request);

    @POST(PATH_PREFIX + "/")
    Call<NetworkResponse.UserInfoLoadResponse> loadUserInfo(@Body NetworkRequest.UserInfoLoadRequest request);

    @POST(PATH_PREFIX + "/")
    Call<NetworkResponse.UserInfoCommitResponse> commitUserInfo(@Body NetworkRequest.UserInfoCommitRequest request);

    @POST(PATH_PREFIX + "/getdetailinfo.php")
    Call<NetworkResponse.StoreInfoLoadResponse> loadStoreInfo(@Body NetworkRequest.StoreInfoLoadRequest request);

    @POST(PATH_PREFIX + "/modifydetailinfo.php")
    Call<NetworkResponse.StoreInfoCommitResponse> commitStoreInfo(@Body NetworkRequest.StoreInfoCommitRequest request);

    @Multipart
    @POST("")
    Call<ResponseBody> uploadFile(@Part("description") RequestBody request, @Part MultipartBody.Part file);
}
