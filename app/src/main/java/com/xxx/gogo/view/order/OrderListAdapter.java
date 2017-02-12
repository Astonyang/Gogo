package com.xxx.gogo.view.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xxx.gogo.R;
import com.xxx.gogo.model.order.OrderItemInfo;
import com.xxx.gogo.model.order.OrderModel;
import com.xxx.gogo.utils.CommonUtils;
import com.xxx.gogo.utils.Constants;

class OrderListAdapter extends BaseAdapter {
    private Activity mContext;
    private int mType;

    OrderListAdapter(Activity context, int type){
        mType = type;
        mContext = context;
    }

    @Override
    public int getCount() {
        return OrderModel.getInstance().getCount(mType);
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

        final OrderItemInfo itemInfo = OrderModel.getInstance().getItem(mType, position);
        viewHolder.tvOrderIndex.setText(itemInfo.id);
        viewHolder.tvState.setText(itemInfo.toStringState(mContext));
        viewHolder.tvShopName.setText(itemInfo.storeName);
        viewHolder.tvOrderPrice.setText(CommonUtils.formatPrice(itemInfo.price));
        viewHolder.tvGoodsNum.setText(String.valueOf(itemInfo.goodsNum));
        viewHolder.tvOrderStartTime.setText(String.valueOf(itemInfo.startTime));

        Drawable drawable = CommonUtils.getStateListDrawable(
                mContext.getResources().getColor(R.color.textColor),
                mContext.getResources().getColor(R.color.pressed_color));
        convertView.setBackgroundDrawable(drawable);
        convertView.setLongClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra(Constants.KEY_ORDER_POSITION, position);
                intent.putExtra(Constants.KEY_ORDER_TYPE, mType);
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

    private static class ViewHolder{
        TextView tvOrderIndex;
        TextView tvState;
        TextView tvShopName;
        TextView tvOrderPrice;
        TextView tvGoodsNum;
        TextView tvOrderStartTime;
    }
}
