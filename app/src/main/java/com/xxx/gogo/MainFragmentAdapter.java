package com.xxx.gogo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xxx.gogo.view.provider.ProviderFragment;
import com.xxx.gogo.view.offen_buy.OffenBuyFragment;
import com.xxx.gogo.view.me.MySelfFragment;
import com.xxx.gogo.view.shopcart.ShopCartFragment;

class MainFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitles;
    private Fragment[] mFragments;
    private int[] mNormalResourceIds;
    private int[] mSelectedResourceIds;

    MainFragmentAdapter(Context context, FragmentManager fm){
        super((fm));

        mTitles = new String[]{
                context.getResources().getString(R.string.favo),
                context.getResources().getString(R.string.provider),
                context.getResources().getString(R.string.shop_cart),
                context.getResources().getString(R.string.myself)
        };

        mNormalResourceIds = new int[]{
                R.drawable.bt_alwby_nm,
                R.drawable.bt_home_nm,
                R.drawable.bt_car_nm,
                R.drawable.bt_user_nm
        };
        mSelectedResourceIds = new int[]{
                R.drawable.bt_alwby_sel,
                R.drawable.bt_home_sel,
                R.drawable.bt_car_sel,
                R.drawable.bt_user_sel
        };
        mFragments = new Fragment[mTitles.length];
        mFragments[0] = new OffenBuyFragment();
        mFragments[1] = new ProviderFragment();
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

    public int getResourceId(int position, boolean selected){
        if(selected){
            return mSelectedResourceIds[position];
        }
        return mNormalResourceIds[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
