package com.xxx.gogo.manager.order;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xxx.gogo.model.order.OrderItemDetailInfo;
import com.xxx.gogo.model.order.OrderItemInfo;
import com.xxx.gogo.net.NetworkInterface;
import com.xxx.gogo.net.VolleyWrapper;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;

class OrderCommitAgent {

    void commit(ArrayList<OrderItemInfo> orderList,
                ArrayList<ArrayList<OrderItemDetailInfo>> detailList,
                final Callback callback){

        VolleyWrapper.getInstance().requestQueue().add(new StringRequest(
                NetworkInterface.COMMIT_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess();
                    }
                }, 2000);
            }
        }));
    }

    void cancel(String orderId, final Callback callback){
        VolleyWrapper.getInstance().requestQueue().add(new StringRequest(
                NetworkInterface.CANCEL_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail();
                    }
                }, 2000);
            }
        }));
    }

    void queryOrderState(String orderId){

    }

    public interface Callback{
        void onSuccess();
        void onFail();
    }
}
