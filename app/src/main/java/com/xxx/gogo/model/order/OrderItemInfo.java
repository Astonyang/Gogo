package com.xxx.gogo.model.order;

import android.content.Context;

import com.xxx.gogo.R;

import java.util.Calendar;

public class OrderItemInfo {
    public static final int STATE_CREATED = 0;
    public static final int STATE_CANCELED = 1;
    public static final int STATE_PENDING = 2;
    public static final int STATE_COMPLETED = 3;
    public static final int STATE_DENIED = 4;

    public String id;
    public int state;
    public String storeName;
    public double price;
    public int goodsNum;
    public long startTime;

    public String toStringState(Context context){
        int resId = -1;
        switch (state){
            case 0:
                resId = R.string.order_state_create;
                break;
            case 1:
                resId = R.string.order_state_canceled;
                break;
            case 2:
                resId = R.string.order_state_pending;
                break;
            case 3:
                resId = R.string.order_state_completed;
                break;
            case 4:
                resId = R.string.order_detail;
                break;
            default:
                break;
        }
        return context.getResources().getString(resId);
    }

    public static String generateOrderId(){
        String id = "";
        Calendar calendar = Calendar.getInstance();
        id += calendar.get(Calendar.YEAR);
        id = id.substring(2);
        id += calendar.get(Calendar.MONTH);
        id += calendar.get(Calendar.DATE);
        id += calendar.get(Calendar.HOUR);
        id += calendar.get(Calendar.MINUTE);
        id += calendar.get(Calendar.SECOND);
        id += calendar.get(Calendar.MILLISECOND);

        //TODO
        //id += userId;

        return id;
    }
}
