package com.xxx.gogo.view.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xxx.gogo.R;
import com.xxx.gogo.model.order.CompletedOrderModel;
import com.xxx.gogo.model.order.OrderItemInfo;
import com.xxx.gogo.model.order.AllOrderModel;
import com.xxx.gogo.model.order.PendingOrderModel;
import com.xxx.gogo.utils.CommonUtils;
import com.xxx.gogo.utils.Constants;

public class OrderListAdapter extends BaseAdapter {
    private Activity mContext;
    private int mType;

    public OrderListAdapter(Activity context, int type){
        mType = type;
        mContext = context;
    }

    @Override
    public int getCount() {
        if(mType == OrderListFragment.TYPE_ALL_ORDER){
            return AllOrderModel.getInstance().getCount();
        }else if(mType == OrderListFragment.TYPE_COMPLETED_ORDER){
            return CompletedOrderModel.getInstance().getCount();
        }else {
            return PendingOrderModel.getInstance().getCount();
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_item, parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.tvOrderIndex = (TextView) convertView.findViewById(R.id.order_num_value);
            viewHolder.tvState= (TextView) convertView.findViewById(R.id.state);
            viewHolder.tvShopName = (TextView) convertView.findViewById(R.id.shop_name);
            viewHolder.tvOrderPrice = (TextView) convertView.findViewById(R.id.order_total_price_value);
            viewHolder.tvOrderStartTime = (TextView) convertView.findViewById(R.id.order_start_time_value);
            viewHolder.tvGoodsNum = (TextView) convertView.findViewById(R.id.goods_num_value);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final OrderItemInfo itemInfo;
        if(mType == OrderListFragment.TYPE_ALL_ORDER){
            itemInfo = AllOrderModel.getInstance().getItem(position);
        }else if(mType == OrderListFragment.TYPE_COMPLETED_ORDER){
            itemInfo = CompletedOrderModel.getInstance().getItem(position);
        }else {
            itemInfo = PendingOrderModel.getInstance().getItem(position);
        }
        viewHolder.tvOrderIndex.setText(itemInfo.id);
        viewHolder.tvState.setText(itemInfo.toStringState(mContext));
        viewHolder.tvShopName.setText(itemInfo.shopName);
        viewHolder.tvOrderPrice.setText(itemInfo.price);
        viewHolder.tvGoodsNum.setText(itemInfo.goodsNum);
        viewHolder.tvOrderStartTime.setText(itemInfo.startTime);

        Drawable drawable = CommonUtils.getStateListDrawable(
                mContext.getResources().getColor(R.color.textColor),
                mContext.getResources().getColor(R.color.pressed_color));
        convertView.setBackgroundDrawable(drawable);
        convertView.setLongClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllOrderModel.getInstance().setDetailItem(itemInfo);
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                mContext.startActivityForResult(intent, Constants.START_ORDER_DETAIL_ACTIVITY);
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

    class ViewHolder{
        TextView tvOrderIndex;
        TextView tvState;
        TextView tvShopName;
        TextView tvOrderPrice;
        TextView tvGoodsNum;
        TextView tvOrderStartTime;
    }
}
