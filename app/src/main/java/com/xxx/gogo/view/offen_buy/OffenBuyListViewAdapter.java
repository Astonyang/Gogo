package com.xxx.gogo.view.offen_buy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.gogo.R;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.offen_buy.OffenBuyModel;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.utils.ToastManager;
import com.xxx.gogo.view.goods.GoodsViewHolder;

public class OffenBuyListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;

    public OffenBuyListViewAdapter(Context context){
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
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final GoodsViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.goods_item,
                    parent, false);
            viewHolder = new GoodsViewHolder();
            viewHolder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.img);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvIntroduce = (TextView) convertView.findViewById(R.id.introduce);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.price);
            viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.index);

            convertView.findViewById(R.id.id_count_container).setVisibility(View.GONE);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsViewHolder) convertView.getTag();
        }

        final GoodsItemInfo info = OffenBuyModel.getInstance().getGoodsItem(childPosition);
        viewHolder.tvName.setText(info.name);
        viewHolder.imageView.setImageURI(info.imgUrl);
        viewHolder.tvPrice.setText(String.valueOf(info.price));
        viewHolder.tvIntroduce.setText(info.introduce);

        final Button button = (Button) convertView.findViewById(R.id.id_add_cart);
        if(ShopCartModel.getInstance().contains(info.generateId())){
            setAddCartButtonState(true, button);
        }else {
            setAddCartButtonState(false, button);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopCartModel.getInstance().addItem(info);
                setAddCartButtonState(true, button);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void setAddCartButtonState(boolean atShopCart, Button button){
        if(atShopCart){
            button.setText(mContext.getString(R.string.already_at_shopcart));
            button.setEnabled(false);
            button.setTextColor(Color.GRAY);
            button.setBackgroundResource(R.drawable.bg_at_shop_cart);
        }else {
            button.setText(mContext.getString(R.string.add_shop_cart));
            button.setEnabled(true);
            button.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            button.setBackgroundResource(R.drawable.login_bg);
        }
    }

    static class GroupViewHolder {
        ImageView imageView;
        TextView tvTitle;
    }
}
