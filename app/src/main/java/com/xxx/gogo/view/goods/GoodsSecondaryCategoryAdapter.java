package com.xxx.gogo.view.goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxx.gogo.R;

public class GoodsSecondaryCategoryAdapter extends BaseExpandableListAdapter {

    String[] groupStrings = {"牛肉", "羊肉", "鸡肉", "猪肉"};
    String[][] childStrings = {
            {"唐三藏", "孙悟空", "猪八戒", "沙和尚"},
            {"宋江", "林冲", "李逵", "鲁智深"},
            {"曹操", "刘备", "孙权", "诸葛亮", "周瑜"},
            {"贾宝玉", "林黛玉", "薛宝钗", "王熙凤"}
    };

    private Context mContext;

    public GoodsSecondaryCategoryAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return groupStrings.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childStrings[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupStrings[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childStrings[groupPosition][childPosition];
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
        groupViewHolder.tvTitle.setText(groupStrings[groupPosition]);
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
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_cart_goods_item,
                    parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) convertView.findViewById(
                    R.id.name);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        //childViewHolder.tvTitle.setText(childStrings[groupPosition][childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        ImageView imageView;
        TextView tvTitle;
    }

    static class ChildViewHolder {
        TextView tvTitle;
    }
}
