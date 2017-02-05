package com.xxx.gogo.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.xxx.gogo.R;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.view.shopcart.GoodsItemCountAdapter;

public class DialogHelper {
    public static Dialog createDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public static void showCountSelectDialog(Context context, final GoodsItemInfo info){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_content_select_count, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

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
