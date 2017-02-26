package com.xxx.gogo.view.goods;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xxx.gogo.model.goods.GoodsCategoryModel;
import com.xxx.gogo.model.goods.GoodsCategoryModelFactory;
import com.xxx.gogo.utils.Constants;

class GoodsCategoryAdapter extends FragmentPagerAdapter {
    private Fragment[] mFragments;
    private String mProviderId;
    private GoodsCategoryModel mModel;

    GoodsCategoryAdapter(FragmentManager fm, String providerId){
        super(fm);

        mProviderId = providerId;

        mModel = GoodsCategoryModelFactory.getModel();
        int count = mModel.getCategoryCount();
        mFragments = new Fragment[count];

        for (int i = 0; i < count; ++i){
            mFragments[i] = new GoodsFragment();

            Bundle b0 = new Bundle();
            b0.putString(Constants.KEY_PROVIDER_ID, mProviderId);
            b0.putString(Constants.KEY_GOODS_CATEGORY_ID, mModel.getCategoryId(i));

            mFragments[i].setArguments(b0);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mModel.getCategoryCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mModel.getCategoryName(position);
    }
}
