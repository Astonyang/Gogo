package com.xxx.gogo.net;

import com.google.gson.annotations.SerializedName;

public class NetworkResponse {
    private static final int RESPONSE_SUCCESS = 1;
    private static final int RESPONSE_FAIL = 0;

    public static class BaseResponse{
        public String key;
        public int result;

        public boolean isSuccessful(){
            return result == RESPONSE_SUCCESS;
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

    public static class ProviderSearchResponse extends BaseResponse {

    }

    public static class ProviderLoadResponse extends BaseResponse {

    }

    public static class ProviderAddResponse extends BaseResponse {

    }

    public static class ProviderRemoveResponse extends BaseResponse{

    }

    public static class OftenbuyLoadResponse extends BaseResponse{

    }

    public static class OftenbuyCommitResponse extends BaseResponse{

    }

    public static class GoodsCategoryLoadResponse extends BaseResponse{

    }

    public static class GoodsSubCategoryLoadResponse extends BaseResponse{

    }

    public static class GoodsLoadResponse extends BaseResponse{

    }

    public static class OrderCommitResponse extends BaseResponse{

    }

    public static class OrderCancelResponse extends BaseResponse{

    }

    public static class OrderModifyResponse extends BaseResponse{

    }

    public static class OrderLoadResponse extends BaseResponse{

    }

    public static class OrderQueryStateResponse extends BaseResponse{

    }

    public static class UserInfoCommitResponse extends BaseResponse{

    }

    public static class UserInfoLoadResponse extends BaseResponse{

    }

    public static class StoreInfoLoadResponse extends BaseResponse{
        @SerializedName("customer_id")
        public String userId;

        @SerializedName("customer_name")
        public String storeOwnerName;

        @SerializedName("customer_shop_name")
        public String storeName;

        @SerializedName("customer_shop_info")
        public String storeInfo;

        @SerializedName("customer_shop_address")
        public String storeAddr;

        @SerializedName("version")
        public String contentVersion;
        public String phone;
        public long earliestSendGoodsTime;
        public long latestSendGoodsTime;
    }

    public static class StoreInfoCommitResponse extends BaseResponse{

    }

//    public static class UploadFileResponse extends BaseResponse{
//
//    }
}
