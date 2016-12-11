package com.xxx.gogo.view.offen_buy;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xxx.gogo.model.offen_buy.OffenBuyModel;

public class OffenBuyListViewAdapter extends BaseAdapter{
    private OffenBuyModel mModel;

    public void setModel(OffenBuyModel model){
        mModel = model;
    }

    @Override
    public int getCount() {
        return mModel.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //convertView = createTextView(position);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.view = convertView;
            //((TextView)convertView).setText(mDataList.get(position));
            //convertView.setTag(viewHolder);
        }else{
            //((TextView)convertView).setText(mDataList.get(position));
        }

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
        View view;
    }
}
