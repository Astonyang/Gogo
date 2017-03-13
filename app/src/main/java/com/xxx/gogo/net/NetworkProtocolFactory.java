package com.xxx.gogo.net;

import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.model.order.OrderItemInfo;

import java.util.List;

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

    public static NetworkRequest.ProviderSearchRequest buildProviderSearchRequest(
            String keyWord, int currentPageNum){
        NetworkRequest.ProviderSearchRequest request =
                new NetworkRequest.ProviderSearchRequest();
        request.keyWord = keyWord;
        request.currentPageNum = currentPageNum;

        return request;
    }

    public static NetworkRequest.ProviderAddRequest buildProviderAddRequest(String providerId){
        NetworkRequest.ProviderAddRequest request = new NetworkRequest.ProviderAddRequest();
        request.providerId = providerId;
        request.userId = UserManager.getInstance().getUserId();
        request.password = UserManager.getInstance().getPassword();

        return request;
    }

    public static NetworkRequest.ProviderRemoveRequest buildProviderRemoveRequest(String providerId){
        NetworkRequest.ProviderRemoveRequest request = new NetworkRequest.ProviderRemoveRequest();
        request.providerId = providerId;
        request.userId = UserManager.getInstance().getUserId();
        request.password = UserManager.getInstance().getPassword();

        return request;
    }

    public static NetworkRequest.ProviderLoadRequest buildProviderLoadRequest(String contentVersion){
        NetworkRequest.ProviderLoadRequest request = new NetworkRequest.ProviderLoadRequest();

        request.contentVersion = contentVersion;
        request.userId = UserManager.getInstance().getUserId();
        request.password = UserManager.getInstance().getPassword();

        return request;
    }

    public static NetworkRequest.OrderCommitRequest buildOrderCommitRequest(List<OrderItemInfo> orders){
        NetworkRequest.OrderCommitRequest request = new NetworkRequest.OrderCommitRequest();
        request.orderItemInfoList = orders;
        return request;
    }

    public static NetworkRequest.OrderCancelRequest buildOrderCancelRequest(String orderId){
        NetworkRequest.OrderCancelRequest request = new NetworkRequest.OrderCancelRequest();

        return request;
    }

    public static NetworkRequest.OrderQueryStateRequest buildOrderQueryStateRequest(){
        NetworkRequest.OrderQueryStateRequest request = new NetworkRequest.OrderQueryStateRequest();

        return request;
    }

    public static NetworkRequest.OrderModifyRequest buildOrderModifyRequest(){
        NetworkRequest.OrderModifyRequest request = new NetworkRequest.OrderModifyRequest();

        return request;
    }

    public static NetworkRequest.UserInfoLoadRequest buildUserInfoLoadRequest(){
        NetworkRequest.UserInfoLoadRequest request = new NetworkRequest.UserInfoLoadRequest();

        return request;
    }

    public static NetworkRequest.UserInfoCommitRequest buildUserInfoCommitRequest(){
        NetworkRequest.UserInfoCommitRequest request = new NetworkRequest.UserInfoCommitRequest();

        return request;
    }

    public static NetworkRequest.StoreInfoLoadRequest buildStoreInfoLoadRequest(){
        NetworkRequest.StoreInfoLoadRequest request = new NetworkRequest.StoreInfoLoadRequest();

        return request;
    }

    public static NetworkRequest.StoreInfoCommitRequest buildStoreInfoCommitRequest(){
        NetworkRequest.StoreInfoCommitRequest request = new NetworkRequest.StoreInfoCommitRequest();

        return request;
    }

    public static NetworkRequest.OftenbufCommitRequest buildOftenBuyCommitRequest(){
        NetworkRequest.OftenbufCommitRequest request = new NetworkRequest.OftenbufCommitRequest();

        return request;
    }

    public static NetworkRequest.OftenbuyLoadRequest buildOftenBuyLoadRequest(){
        NetworkRequest.OftenbuyLoadRequest request = new NetworkRequest.OftenbuyLoadRequest();

        return request;
    }

    public static NetworkRequest.GoodsCategoryLoadRequest buildGoodsCategoryRequest(){
        NetworkRequest.GoodsCategoryLoadRequest request = new NetworkRequest.GoodsCategoryLoadRequest();

        return request;
    }
//
//    public static NetworkRequest.GoodsSubCategoryLoadRequest buildSubGoodsCategoryRequest(){
//        NetworkRequest.GoodsSubCategoryLoadRequest request = new NetworkRequest.GoodsSubCategoryLoadRequest();
//
//        return request;
//    }

    public static NetworkRequest.GoodsLoadRequest buildGoodsLoadRequest(){
        NetworkRequest.GoodsLoadRequest request = new NetworkRequest.GoodsLoadRequest();

        return request;
    }

//    public static NetworkRequest.UploadFileRequest buildFileUploadRequest(){
//        NetworkRequest.UploadFileRequest request = new NetworkRequest.UploadFileRequest();
//
//        return request;
//    }
}
