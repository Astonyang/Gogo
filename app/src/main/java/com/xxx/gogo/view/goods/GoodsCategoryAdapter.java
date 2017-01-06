package com.xxx.gogo.view.goods;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxx.gogo.R;
import com.xxx.gogo.model.goods.GoodsCategoryModel;

public class GoodsCategoryAdapter extends BaseAdapter {
    private GoodsCategoryModel mModel;
    private Context mContext;

    public GoodsCategoryAdapter(Context context, GoodsCategoryModel model){
        mModel = model;
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.goods_category_item,
                    parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.name);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String testUrl = "";//http://goo.gl/gEgYUd";

        Uri uri = Uri.parse(testUrl);
        viewHolder.imgView.setImageURI(uri);
        viewHolder.textView.setText(mModel.getName(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                mContext.startActivity(intent);
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

    @Override
    public int getCount() {
        return mModel.getCount();
    }

    class ViewHolder{
        ImageView imgView;
        TextView textView;
    }
}
