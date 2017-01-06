package com.xxx.gogo.net;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class RequestHelper {

    public static class StringRequestWithoutEcho extends StringRequest{
        public StringRequestWithoutEcho(String url){
            super(url, new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }
}
