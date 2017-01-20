package com.xxx.gogo.view.goods;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.gogo.R;
import com.xxx.gogo.model.goods.GoodsCategoryModel;

public class GoodsCategoryAdapter2 extends
        RecyclerView.Adapter <GoodsCategoryAdapter2.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    private GoodsCategoryModel mModel;
    private View mHeader;

    public GoodsCategoryAdapter2(GoodsCategoryModel model, View header) {
        super();
        mModel = model;
        mHeader = header;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position == 0){
            return;
        }
        SimpleDraweeView imgView = (SimpleDraweeView) holder.itemView.findViewById(R.id.img);
        final TextView textView = (TextView) holder.itemView.findViewById(R.id.name);

        final String testUrl = "";//http://goo.gl/gEgYUd";

        Uri uri = Uri.parse(testUrl);
        imgView.setImageURI(uri);
        textView.setText(mModel.getName(position -1));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(textView.getContext(), GoodsDetailActivity.class);
                textView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_VIEW_TYPE_HEADER){
            return new ViewHolder(mHeader);
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.goods_category_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mModel.getCount() + 1;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return ITEM_VIEW_TYPE_HEADER;
        }
        return ITEM_VIEW_TYPE_ITEM;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
