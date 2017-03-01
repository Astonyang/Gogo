package com.xxx.gogo.view.order;

import android.content.Context;
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
import com.xxx.gogo.model.order.OrderConfirmModel;
import com.xxx.gogo.utils.CommonUtils;

class OrderConfirmAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private OrderConfirmModel mModel;

    OrderConfirmAdapter(Context context, OrderConfirmModel model){
        mContext = context;
        mModel = model;
    }

    @Override
    public int getGroupCount() {
        return mModel.getGroupCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mModel.getChildCount(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mModel.getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mModel.getItem(groupPosition, childPosition);
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
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.order_confirm_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.name);
            groupViewHolder.imageView = (ImageView) convertView.findViewById(R.id.img);
            groupViewHolder.tvProviderName = (TextView) convertView.findViewById(R.id.provider_name);
            groupViewHolder.tvTotalTip = (TextView) convertView.findViewById(R.id.id_total_tip);
            groupViewHolder.tvTotalPrice = (TextView) convertView.findViewById(R.id.id_total_price);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTotalPrice.setText(CommonUtils.formatPrice(
                mModel.getTotalPriceForFroup(groupPosition)));
        groupViewHolder.tvProviderName.setText(mModel.getGroup(groupPosition));
        groupViewHolder.tvTitle.setText(mContext.getString(R.string.order) + " " + (groupPosition + 1));
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
        ItemViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_confirm_item,
                    parent, false);
            viewHolder = new ItemViewHolder();
            viewHolder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.img);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.price);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.num);
            viewHolder.tvState = (TextView) convertView.findViewById(R.id.id_state_tv);
            viewHolder.countContainer = convertView.findViewById(R.id.id_num_container);
            viewHolder.btnDelete = (Button) convertView.findViewById(R.id.id_delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHolder) convertView.getTag();
        }

        GoodsItemInfo info = mModel.getItem(groupPosition, childPosition);
        viewHolder.tvName.setText(info.name);
        viewHolder.imageView.setImageURI(info.imgUrl);

        if(info.state == GoodsItemInfo.STATE_UNDERCARRIAGE){
            viewHolder.countContainer.setVisibility(View.GONE);
            viewHolder.tvState.setVisibility(View.VISIBLE);
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
            viewHolder.tvPrice.setVisibility(View.INVISIBLE);

            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModel.deleteItem(groupPosition, childPosition);
                }
            });

            viewHolder.tvState.setText(mContext.getString(R.string.goods_under_carriage));
        }else {
            viewHolder.countContainer.setVisibility(View.VISIBLE);
            viewHolder.tvState.setVisibility(View.GONE);
            viewHolder.btnDelete.setVisibility(View.GONE);
            viewHolder.tvPrice.setVisibility(View.VISIBLE);

            viewHolder.tvCount.setText(String.valueOf(info.count));
            viewHolder.tvPrice.setText(CommonUtils.formatPrice(info.price));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static private class GroupViewHolder {
        ImageView imageView;
        TextView tvTitle;
        TextView tvProviderName;
        TextView tvTotalTip;
        TextView tvTotalPrice;
    }

    static private class ItemViewHolder{
        SimpleDraweeView imageView;
        TextView tvName;
        TextView tvPrice;
        TextView tvCount;
        TextView tvState;
        View countContainer;
        Button btnDelete;
    }
}
