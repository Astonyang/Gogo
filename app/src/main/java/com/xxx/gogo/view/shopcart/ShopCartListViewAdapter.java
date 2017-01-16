package com.xxx.gogo.view.shopcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.gogo.R;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.view.goods.GoodsViewHolder;

public class ShopCartListViewAdapter extends BaseAdapter{
    private Context mContext;

    public ShopCartListViewAdapter(Context context){
        mContext = context;
        ShopCartModel.getInstance().load();
    }

    @Override
    public int getCount() {
        return ShopCartModel.getInstance().getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodsViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new GoodsViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_cart_goods_item,
                    parent, false);
            viewHolder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.img);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvIntroduce = (TextView) convertView.findViewById(R.id.introduce);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.price);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.num);
            viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.index);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (GoodsViewHolder)convertView.getTag();
        }
        GoodsItemInfo info = ShopCartModel.getInstance().getGoodsItem(position);
        viewHolder.tvName.setText(info.name);
        viewHolder.imageView.setImageURI(info.imgUrl);
        viewHolder.tvCount.setText("0");
        viewHolder.tvIndex.setText(String.valueOf(position) + ".");
        viewHolder.tvPrice.setText(info.price);
        viewHolder.tvIntroduce.setText(info.introduce);

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
}
