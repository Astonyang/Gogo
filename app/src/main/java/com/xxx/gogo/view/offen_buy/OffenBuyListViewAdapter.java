package com.xxx.gogo.view.offen_buy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.gogo.R;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.offen_buy.OffenBuyModel;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.utils.DialogHelper;
import com.xxx.gogo.view.goods.GoodsViewHolder;

public class OffenBuyListViewAdapter extends BaseExpandableListAdapter {
    private Activity mContext;

    static private class GroupViewHolder {
        ImageView imageView;
        TextView tvTitle;
    }

    OffenBuyListViewAdapter(Activity context){
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return OffenBuyModel.getInstance().getGroupCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return OffenBuyModel.getInstance().getChildrenCount(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return OffenBuyModel.getInstance().getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return OffenBuyModel.getInstance().getChild(groupPosition, childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.goods_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.name);
            groupViewHolder.imageView = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(String.valueOf(OffenBuyModel.getInstance().getGroup(groupPosition)));
        if(isExpanded){
            groupViewHolder.imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
        }else {
            groupViewHolder.imageView.setImageResource(R.drawable.ic_chevron_right_black_24dp);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final GoodsViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.goods_item, parent, false);
            viewHolder = new GoodsViewHolder();
            viewHolder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.img);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvIntroduce = (TextView) convertView.findViewById(R.id.introduce);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.price);
            viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.index);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.id_count_value);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsViewHolder) convertView.getTag();
        }

        final GoodsItemInfo info = OffenBuyModel.getInstance().getGoodsItem(groupPosition, childPosition);
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

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
