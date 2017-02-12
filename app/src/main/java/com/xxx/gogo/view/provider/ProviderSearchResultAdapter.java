package com.xxx.gogo.view.provider;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xxx.gogo.BusEvent;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.model.provider.ProviderItemInfo;
import com.xxx.gogo.model.provider.ProviderModel;
import com.xxx.gogo.model.provider.ProviderSearcher;
import com.xxx.gogo.view.user.LoginActivity;

class ProviderSearchResultAdapter extends BaseAdapter{
    private Activity mActivity;
    private ProviderSearcher mDataSource;

    ProviderSearchResultAdapter(Activity activity, ProviderSearcher dataSource){
        mDataSource = dataSource;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mDataSource.getCount();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.provider_search_result_item, parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.button = (Button) convertView.findViewById(R.id.add);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.contact_name);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final ProviderItemInfo providerItemInfo = mDataSource.getItem(position);
        viewHolder.textView.setText(providerItemInfo.name);

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserManager.getInstance().isLogin()){
                    ProviderModel.getInstance().addItem(providerItemInfo);
                    mActivity.finish();
                    BusFactory.getBus().post(new BusEvent.TabSwitcher(BusEvent.TabSwitcher.TAB_PROVIDER));
                }else {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ProviderDetailActivity.class);
                ProviderItemDetailModel.getInstance().setProviderItem(providerItemInfo);
                mActivity.startActivity(intent);
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

    static private class ViewHolder{
        TextView textView;
        Button button;
    }
}
