//package com.xxx.gogo.net;
//
//import android.content.Context;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;
//
//public class VolleyWrapper {
//    private RequestQueue mQueue;
//
//    private static class InstanceHolder{
//        private static VolleyWrapper sInstance = new VolleyWrapper();
//    }
//
//    public static VolleyWrapper getInstance(){
//        return InstanceHolder.sInstance;
//    }
//
//    private VolleyWrapper(){
//
//    }
//
//    public void init(Context context){
//        mQueue = Volley.newRequestQueue(context);
//    }
//
//    public RequestQueue requestQueue(){
//        return mQueue;
//    }
//}
