package com.xxx.gogo.view.shopcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxx.gogo.R;
import com.xxx.gogo.model.shopcart.ShopCartModel;

public class ShopCartListViewAdapter extends BaseAdapter{
    private Context mContext;

    public ShopCartListViewAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return ShopCartModel.getInstance().getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.goods_item,
                    parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvIntroduce = (TextView) convertView.findViewById(R.id.introduce);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.price);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.num);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        //viewHolder.tvName
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

    class ViewHolder{
        ImageView imageView;
        TextView tvName;
        TextView tvIntroduce;
        TextView tvPrice;
        TextView tvCount;
    }
}
