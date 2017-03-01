package com.xxx.gogo.view.goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.gogo.R;
import com.xxx.gogo.model.PagedModel;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.utils.DialogHelper;

class GoodsListAdapter extends BaseAdapter {
    private Context mContext;
    private PagedModel<GoodsItemInfo> mModel;

    GoodsListAdapter(Context context, PagedModel<GoodsItemInfo> model){
        mContext = context;
        mModel = model;
    }

    @Override
    public int getCount() {
        return mModel.getCount();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        GoodsViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.goods_item,
                    parent, false);
            viewHolder = new GoodsViewHolder();
            viewHolder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.img);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvIntroduce = (TextView) convertView.findViewById(R.id.introduce);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.price);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.id_count_value);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsViewHolder) convertView.getTag();
        }

        final GoodsItemInfo info = mModel.getItem(position);

        viewHolder.tvName.setText(info.name);
        viewHolder.imageView.setImageURI(info.imgUrl);
        viewHolder.tvPrice.setText(String.valueOf(info.price));
        viewHolder.tvIntroduce.setText(info.introduce);

        String id = info.generateId();
        if(ShopCartModel.getInstance().contains(id)){
            info.count = ShopCartModel.getInstance().getGoodsItem(id).count;
        }
        viewHolder.tvCount.setText(String.valueOf(info.count));

        viewHolder.tvCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showCountSelectDialog(mContext, info);
            }
        });

        ((GoodsItemView)convertView).setGoodsInfo(info);
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
