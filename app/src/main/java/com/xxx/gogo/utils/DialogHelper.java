package com.xxx.gogo.utils;

import android.app.Dialog;
import android.content.Context;

import com.xxx.gogo.R;

public class DialogHelper {
    public static Dialog createDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
