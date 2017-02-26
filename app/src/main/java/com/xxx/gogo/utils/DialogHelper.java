package com.xxx.gogo.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.xxx.gogo.R;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.view.shopcart.GoodsItemCountAdapter;

public class DialogHelper {

    public static Dialog showDialog(Context context, String content){
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_info_view, null);
        TextView textView = (TextView) root.findViewById(R.id.id_tip);
        textView.setText(content);

        final Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(root);
        dialog.setCanceledOnTouchOutside(false);

        Window dialogWindow = dialog.getWindow();
        if(dialogWindow != null){
            WindowManager.LayoutParams p = dialogWindow.getAttributes();
            p.gravity = Gravity.CENTER;
            p.height = (int) (CommonUtils.getScreenHeight(context) * 0.15);
            p.width = (int) (CommonUtils.getScreenWidth(context) * 0.25);
            dialogWindow.setAttributes(p);
        }

        dialog.show();

        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 600);

        return dialog;
    }

    public static Dialog showLoadingDialog(Context context, String content){
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_loading_view, null);
        TextView textView = (TextView) root.findViewById(R.id.id_tip);
        textView.setText(content);

        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(root);
        dialog.setCanceledOnTouchOutside(false);

        Window dialogWindow = dialog.getWindow();
        if(dialogWindow != null){
            WindowManager.LayoutParams p = dialogWindow.getAttributes();
            p.height = (int) (CommonUtils.getScreenHeight(context) * 0.25);
            p.width = (int) (CommonUtils.getScreenWidth(context) * 0.3);
            p.gravity = Gravity.CENTER;

            dialogWindow.setAttributes(p);
        }

        dialog.show();

        return dialog;
    }

    public static void showCountSelectDialog(Context context, final GoodsItemInfo info){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_content_select_count, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        Window dialogWindow = dialog.getWindow();
        if(dialogWindow != null){
            WindowManager.LayoutParams p = dialogWindow.getAttributes();
            p.gravity = Gravity.CENTER;
            p.height = (int) (CommonUtils.getScreenHeight(context) * 0.6);
            p.width = (int) (CommonUtils.getScreenWidth(context) * 0.45);
            dialogWindow.setAttributes(p);
        }

        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(new GoodsItemCountAdapter(context,
                new GoodsItemCountAdapter.OnItemSelected() {
                    @Override
                    public void onSelected(int position) {
                        if(position == 0){
                            if(ShopCartModel.getInstance().contains(info.generateId())){
                                ShopCartModel.getInstance().deleteItem(info);
                            }
                        }else {
                            if(ShopCartModel.getInstance().contains(info.generateId())){
                                double oldValue = info.count * info.price;
                                info.count = position;
                                ShopCartModel.getInstance().getGoodsItem(info.generateId()).count = position;
                                double newValue = info.count * info.price;
                                ShopCartModel.getInstance().modifyTotalPrice(oldValue, newValue);
                            }else {
                                info.count = position;
                                ShopCartModel.getInstance().addItem(info);
                            }
                        }

                        dialog.dismiss();
                    }
                }));
    }
}
