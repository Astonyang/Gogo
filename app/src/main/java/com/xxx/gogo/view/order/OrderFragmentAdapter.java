package com.xxx.gogo.view.order;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xxx.gogo.R;
import com.xxx.gogo.model.order.AllOrderModel;

public class OrderFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitles;
    private Fragment[] mFragments;

    public OrderFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);

        mTitles = new String[]{
                context.getResources().getString(R.string.all_order),
                context.getResources().getString(R.string.pending_order),
                context.getResources().getString(R.string.completed_order),
        };

        mFragments = new Fragment[mTitles.length];

        mFragments[0] = new OrderListFragment();
        Bundle b0 = new Bundle();
        b0.putInt("type", OrderListFragment.TYPE_ALL_ORDER);
        mFragments[0].setArguments(b0);

        mFragments[1] = new OrderListFragment();
        Bundle b1 = new Bundle();
        b1.putInt("type", OrderListFragment.TYPE_PENDING_ORDER);
        mFragments[1].setArguments(b1);

        mFragments[2] = new OrderListFragment();
        Bundle b2 = new Bundle();
        b2.putInt("type", OrderListFragment.TYPE_COMPLETED_ORDER);
        mFragments[2].setArguments(b2);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
