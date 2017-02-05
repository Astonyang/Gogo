package com.xxx.gogo.view.shopcart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.gogo.R;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.utils.DialogHelper;
import com.xxx.gogo.view.goods.GoodsViewHolder;

class ShopCartListViewAdapter extends BaseAdapter{
    private Context mContext;

    ShopCartListViewAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return ShopCartModel.getInstance().getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GoodsViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new GoodsViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_cart_goods_item,
                    parent, false);
            viewHolder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.img);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvIntroduce = (TextView) convertView.findViewById(R.id.introduce);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.price);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.id_count_value);
            viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.index);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (GoodsViewHolder)convertView.getTag();
        }

        final GoodsItemInfo info = ShopCartModel.getInstance().getGoodsItem(position);
        viewHolder.tvName.setText(info.name);
        viewHolder.imageView.setImageURI(info.imgUrl);
        viewHolder.tvCount.setText(String.valueOf(info.count));
        viewHolder.tvCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showCountSelectDialog(mContext, info);
            }
        });
        //TODO
        viewHolder.tvIndex.setText(String.valueOf(position+1) + ".");
        viewHolder.tvPrice.setText(String.valueOf(info.price));
        viewHolder.tvIntroduce.setText(info.introduce);

        convertView.findViewById(R.id.goods_item_at_cart).setOnLongClickListener(
                new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteGoods(info);
                return true;
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    private void deleteGoods(final GoodsItemInfo info){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.delete_goods_from_cart);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ShopCartModel.getInstance().deleteItem(info);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
