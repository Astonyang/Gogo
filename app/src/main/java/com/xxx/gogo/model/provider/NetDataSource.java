package com.xxx.gogo.model.provider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xxx.gogo.net.NetworkInterface;
import com.xxx.gogo.net.RequestHelper.StringRequestWithoutEcho;
import com.xxx.gogo.net.VolleyWrapper;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;

class NetDataSource {
    private LocalDataSource mCb;

    private StringRequest loadDataRequest = new StringRequest(Request.Method.GET,
            NetworkInterface.LOAD_PROVIDER_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
                        @Override
                        public void run() {
                            mCb.onDataReady(null);
                        }
                    });
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mCb.onDataReady(getTestData());
        }
    });

    NetDataSource(LocalDataSource callback){
        mCb = callback;
    }

    void addItem(ProviderItemInfo info){
        StringRequestWithoutEcho request = new StringRequestWithoutEcho(
                NetworkInterface.ADD_PROVIDER_URL);
        VolleyWrapper.getInstance().requestQueue().add(request);
    }

    void deleteItem(ProviderItemInfo info){
        StringRequestWithoutEcho request = new StringRequestWithoutEcho(
                NetworkInterface.DELETE_PROVIDER_URL);
        VolleyWrapper.getInstance().requestQueue().add(request);
    }

    void load(){
        VolleyWrapper.getInstance().requestQueue().add(loadDataRequest);
    }

    private ArrayList<ProviderItemInfo> getTestData(){
        ArrayList<ProviderItemInfo> datas = new ArrayList<>();
        for (int i = 0; i < 20; ++i){
            ProviderItemInfo info = new ProviderItemInfo();
            info.id = String.valueOf(i);
            info.name = "Provider_" + String.valueOf(i);
            info.phone = String.valueOf(10000000 + i);
            info.addr = "Wall Street 123";
            info.url = "www.google.com";
            info.lat = 23.1;
            info.lng = 12.3;
            datas.add(info);
        }

        return datas;
    }
}
