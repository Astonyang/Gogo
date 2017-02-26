package com.xxx.gogo.view.order;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xxx.gogo.R;
import com.xxx.gogo.model.order.OrderModel;
import com.xxx.gogo.utils.Constants;

class OrderFragmentAdapter extends FragmentPagerAdapter implements OrderModel.Callback{
    private String[] mTitles;
    private OrderListFragment[] mFragments;

    OrderFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);

        mTitles = new String[]{
                context.getResources().getString(R.string.all_order),
                context.getResources().getString(R.string.pending_order),
                context.getResources().getString(R.string.completed_order),
        };

        mFragments = new OrderListFragment[mTitles.length];

        mFragments[0] = new OrderListFragment();
        Bundle b0 = new Bundle();
        b0.putInt(Constants.KEY_ORDER_TYPE, OrderModel.TYPE_ALL);
        mFragments[0].setArguments(b0);

        mFragments[1] = new OrderListFragment();
        Bundle b1 = new Bundle();
        b1.putInt(Constants.KEY_ORDER_TYPE, OrderModel.TYPE_PENDING);
        mFragments[1].setArguments(b1);

        mFragments[2] = new OrderListFragment();
        Bundle b2 = new Bundle();
        b2.putInt(Constants.KEY_ORDER_TYPE, OrderModel.TYPE_COMPLETED);
        mFragments[2].setArguments(b2);

        OrderModel.getInstance().setCallback(this);
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

    @Override
    public void onLoaded() {
        mFragments[0].notifyOrderLoaded();
        mFragments[1].notifyOrderLoaded();
        mFragments[2].notifyOrderLoaded();
    }
}
