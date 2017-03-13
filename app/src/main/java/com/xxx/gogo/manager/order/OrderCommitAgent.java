package com.xxx.gogo.manager.order;

import com.xxx.gogo.model.order.OrderItemDetailInfo;
import com.xxx.gogo.model.order.OrderItemInfo;
import com.xxx.gogo.net.NetworkProtocolFactory;
import com.xxx.gogo.net.NetworkResponse;
import com.xxx.gogo.net.NetworkServiceFactory;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

class OrderCommitAgent {

    void commit(List<OrderItemInfo> orderList,
                List<List<OrderItemDetailInfo>> detailList,
                final Callback callback){
        Call<NetworkResponse.OrderCommitResponse> call = NetworkServiceFactory.getInstance()
                .getService().commitOrder(NetworkProtocolFactory.buildOrderCommitRequest(orderList));
        call.enqueue(new retrofit2.Callback<NetworkResponse.OrderCommitResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.OrderCommitResponse> call,
                                   Response<NetworkResponse.OrderCommitResponse> response) {
                if(callback == null){
                    return;
                }
                if(response.isSuccessful() && response.body().isSuccessful()){
                    callback.onSuccess();
                }else {
                    callback.onFail();
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.OrderCommitResponse> call, Throwable t) {
                if(callback != null){
                    callback.onFail();
                }
            }
        });
    }

    void cancel(String orderId, final Callback callback){
        Call<NetworkResponse.OrderCancelResponse> call = NetworkServiceFactory.getInstance()
                .getService().cancelOrder(NetworkProtocolFactory.buildOrderCancelRequest(orderId));

        call.enqueue(new retrofit2.Callback<NetworkResponse.OrderCancelResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.OrderCancelResponse> call,
                                   Response<NetworkResponse.OrderCancelResponse> response) {
                if(callback == null){
                    return;
                }
                if(response.isSuccessful() && response.body().isSuccessful()){
                    callback.onSuccess();
                }else {
                    callback.onFail();
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.OrderCancelResponse> call, Throwable t) {
                if(callback != null){
                    callback.onFail();
                }
            }
        });
    }

    void queryOrderState(final QueryStateCallback callback){
        Call<NetworkResponse.OrderQueryStateResponse> call = NetworkServiceFactory.getInstance()
                .getService().queryOrderState(NetworkProtocolFactory.buildOrderQueryStateRequest());
        call.enqueue(new retrofit2.Callback<NetworkResponse.OrderQueryStateResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.OrderQueryStateResponse> call,
                                   Response<NetworkResponse.OrderQueryStateResponse> response) {
                if(callback == null){
                    return;
                }
                if(response.isSuccessful() && response.body().isSuccessful()){
                    callback.onState(null);
                }else {
                    callback.onState(null);
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.OrderQueryStateResponse> call, Throwable t) {
                if(callback != null){
                    callback.onState(null);
                }
            }
        });
    }

    void modify(){

    }

    public interface Callback{
        void onSuccess();
        void onFail();
    }

    public interface QueryStateCallback{
        void onState(Map<String, Integer> stateMap);
    }
}
