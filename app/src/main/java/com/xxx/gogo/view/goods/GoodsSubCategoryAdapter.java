package com.xxx.gogo.view.goods;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.xxx.gogo.model.goods.GoodsSubCategoryModel;
import com.xxx.gogo.utils.Constants;

class GoodsSubCategoryAdapter extends FragmentPagerAdapter {
    private GoodsSubCategoryModel mModel;
    private SparseArray<Fragment> mFragments;
    private String mProviderId;
    private String mParentCatId;

    private GoodsSubCategoryActivity mActivity;

    GoodsSubCategoryAdapter(FragmentManager fm,
                            GoodsSubCategoryActivity activity,
                            String providerId,
                            String parentCatId,
                            GoodsSubCategoryModel model) {
        super(fm);

        mActivity = activity;
        mModel = model;
        mFragments = new SparseArray<>();
        mProviderId = providerId;
        mParentCatId = parentCatId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = mFragments.get(position);
        if(f == null){
            GoodsFragment fragment = new GoodsFragment();
            fragment.setActivity(mActivity);

            Bundle b0 = new Bundle();
            b0.putString(Constants.KEY_PROVIDER_ID, mProviderId);
            b0.putString(Constants.KEY_GOODS_CATEGORY_ID, mModel.getCategoryId(position));
            b0.putString(Constants.KEY_GOODS_PARENT_CATEGORY_ID, mParentCatId);
            fragment.setArguments(b0);

            mFragments.put(position, fragment);
        }
        return mFragments.get(position);
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