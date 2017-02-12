package com.xxx.gogo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.view.provider.ProviderFragment;
import com.xxx.gogo.view.often_buy.OftenBuyFragment;
import com.xxx.gogo.view.me.MySelfFragment;
import com.xxx.gogo.view.shopcart.ShopCartFragment;

class MainFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String[] mTitles;
    private Fragment[] mFragments;
    private int[] mNormalResourceIds;
    private int[] mSelectedResourceIds;

    MainFragmentAdapter(Context context, FragmentManager fm){
        super((fm));

        mContext = context;

        mTitles = new String[]{
                context.getResources().getString(R.string.provider),
                context.getResources().getString(R.string.favo),
                context.getResources().getString(R.string.shop_cart),
                context.getResources().getString(R.string.myself)
        };

        mNormalResourceIds = new int[]{
                R.drawable.bt_home_nm,
                R.drawable.bt_alwby_nm,
                R.drawable.bt_car_nm,
                R.drawable.bt_user_nm
        };
        mSelectedResourceIds = new int[]{
                R.drawable.bt_home_sel,
                R.drawable.bt_alwby_sel,
                R.drawable.bt_car_sel,
                R.drawable.bt_user_sel
        };
        mFragments = new Fragment[mTitles.length];
        mFragments[0] = new ProviderFragment();
        mFragments[1] = new OftenBuyFragment();
        mFragments[2] = new ShopCartFragment();
        mFragments[3] = new MySelfFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    int getResourceId(int position, boolean selected){
        if(selected){
            return mSelectedResourceIds[position];
        }
        return mNormalResourceIds[position];
    }

    View getTabView(int pos, boolean selected){
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.tab_main_custom_view, null);
        ImageView imageView = (ImageView) tabView.findViewById(R.id.id_img);
        imageView.setImageResource(getResourceId(pos, selected));

        TextView tvTitle = (TextView) tabView.findViewById(R.id.id_title);
        tvTitle.setText(mTitles[pos]);

        if(pos == 2){
            TextView badgeView = (TextView) tabView.findViewById(R.id.id_badge);
            badgeView.setVisibility(View.VISIBLE);
            int count = ShopCartModel.getInstance().getCount();
            count = count > 99 ? 99 : count;
            if(count == 0){
                badgeView.setVisibility(View.INVISIBLE);
            }else {
                badgeView.setVisibility(View.VISIBLE);
            }
            badgeView.setText(String.valueOf(count));
        }
        return tabView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
