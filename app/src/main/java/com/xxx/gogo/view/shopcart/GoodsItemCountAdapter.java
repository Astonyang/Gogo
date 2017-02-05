package com.xxx.gogo.view.shopcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xxx.gogo.R;

public class GoodsItemCountAdapter extends BaseAdapter {
    private Context mContext;
    private OnItemSelected mSelectedListener;

    public GoodsItemCountAdapter(Context context, OnItemSelected listener) {
        super();
        mContext = context;
        mSelectedListener = listener;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.googs_count_select_item,
                    parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tvCount);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.textView.setText(String.valueOf(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedListener.onSelected(position);
            }
        });
        convertView.setBackgroundResource(R.drawable.selector_goods_count_item);
        return convertView;
    }

    private class ViewHolder{
        TextView textView;
    }

    public interface OnItemSelected{
        void onSelected(int position);
    }
}
