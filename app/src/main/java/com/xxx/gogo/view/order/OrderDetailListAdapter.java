package com.xxx.gogo.view.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xxx.gogo.R;
import com.xxx.gogo.model.order.OrderDetailGoodsItem;
import com.xxx.gogo.model.order.OrderDetailGoodsItemModel;

public class OrderDetailListAdapter extends BaseAdapter
        implements OrderDetailGoodsItemModel.Callback{
    private Context mContext;
    private OrderDetailGoodsItemModel mModel;

    public OrderDetailListAdapter(Context context){
        mContext = context;
        mModel = new OrderDetailGoodsItemModel(this);
        mModel.load();
    }

    @Override
    public int getCount() {
        return mModel.getCount();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_detail_goods_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.count);
            viewHolder.tvActualCount = (TextView) convertView.findViewById(R.id.actual_count);
            viewHolder.tvUnitPrice = (TextView) convertView.findViewById(R.id.unit_price);
            viewHolder.tvTotalPrice = (TextView) convertView.findViewById(R.id.total_price);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        OrderDetailGoodsItem item = mModel.getItem(position);
        viewHolder.tvName.setText(item.name);
        viewHolder.tvCount.setText(String.valueOf(item.count));
        viewHolder.tvActualCount.setText(String.valueOf(item.actualCount));
        viewHolder.tvUnitPrice.setText(String.valueOf(item.unitPrice));
        viewHolder.tvTotalPrice.setText(String.valueOf(item.totalPrice));

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
    public void onSuccess() {
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tvName;
        TextView tvCount;
        TextView tvActualCount;
        TextView tvUnitPrice;
        TextView tvTotalPrice;
    }
}
