package com.xxx.gogo.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastManager {

    public static void showToast(Context context, String content){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, content, duration);
        toast.show();
    }
}
