package com.xxx.gogo.net;

import com.google.gson.annotations.SerializedName;
import com.xxx.gogo.BuildConfig;
import com.xxx.gogo.model.order.OrderItemInfo;

import java.util.List;

public class NetworkRequest {

    public static class BaseRequest{
        @SerializedName("version")
        public String clientVersion = BuildConfig.VERSION_NAME;

        @SerializedName("platform")
        public String clientPlatform = "3";
    }

    public static class UserRelatedRequest{
        @SerializedName("customer_id")
        public String userId;

        @SerializedName("customer_password")
        public String password;
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
        int currentPageNum;
        String keyWord;
        String contentVersion = "";
        String lat;
        String lng;
    }

    static class ProviderLoadRequest extends UserRelatedRequest{
        String contentVersion;
    }

    static class ProviderAddRequest extends UserRelatedRequest{
        String providerId;
        String operation = "1";
    }

    static class ProviderRemoveRequest extends UserRelatedRequest{
        String providerId;
        String operation = "2";
    }

    static class OftenbuyLoadRequest extends BaseRequest{

    }

    static class OftenbufCommitRequest extends BaseRequest{

    }

    static class GoodsCategoryLoadRequest extends BaseRequest{

    }

//    static class GoodsSubCategoryLoadRequest extends BaseRequest{
//
//    }

    static class GoodsLoadRequest extends BaseRequest{

    }

    public static class OrderCommitRequest extends UserRelatedRequest{
        public List<OrderItemInfo> orderItemInfoList;
    }

    static class OrderCancelRequest extends BaseRequest{

    }

    static class OrderModifyRequest extends BaseRequest{

    }

    static class OrderLoadRequest extends BaseRequest{

    }

    static class OrderQueryStateRequest extends BaseRequest{

    }

    static class UserInfoCommitRequest extends UserRelatedRequest{

    }

    static class UserInfoLoadRequest extends UserRelatedRequest{

    }

    static class StoreInfoLoadRequest extends UserRelatedRequest{
        @SerializedName("content_ver")
        public String contentVersion;
    }

    static class StoreInfoCommitRequest extends UserRelatedRequest{
    }

//    static class UploadFileRequest extends BaseRequest{
//
//    }
}
