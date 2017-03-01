package com.xxx.gogo.view.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.model.goods.GoodsCategoryModelFactory;
import com.xxx.gogo.model.goods.GoodsSubCategoryModel;
import com.xxx.gogo.utils.Constants;

public class GoodsSubCategoryActivity extends BaseToolBarActivity
        implements View.OnClickListener, GoodsSubCategoryModel.Callback{

    private GoodsSubCategoryModel mModel;
    private GoodsSubCategoryAdapter mAdapter;
    private String mProviderId;
    private String mGoodsCategoryId;

    private View mLoadingView;
    private View mFailView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goods_detail);

        Intent intent = getIntent();
        if(intent != null){
            mProviderId = intent.getStringExtra(Constants.KEY_PROVIDER_ID);
            mGoodsCategoryId = intent.getStringExtra(Constants.KEY_GOODS_PARENT_CATEGORY_ID);
        }

        createNormalToolBar(R.string.goods_list, this);

        mModel = GoodsCategoryModelFactory.createSubCategoryModel(
                this, mProviderId, mGoodsCategoryId);
        mModel.load();

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView(){
        mLoadingView = findViewById(R.id.id_loading);
        mFailView = findViewById(R.id.id_btn_load_again);

        mFailView.setVisibility(View.INVISIBLE);
        mFailView.setOnClickListener(this);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        mAdapter = new GoodsSubCategoryAdapter(getSupportFragmentManager(), this,
                mProviderId, mGoodsCategoryId, mModel);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (v.getId() == R.id.id_btn_load_again){
            mLoadingView.setVisibility(View.VISIBLE);
            mFailView.setVisibility(View.INVISIBLE);

            mModel.load();
        }
    }

    @Override
    public void onLoadFail() {
        mLoadingView.setVisibility(View.INVISIBLE);
        mFailView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadSuccess() {
        mAdapter.notifyDataSetChanged();
    }

    //TODO fix it later
    public void displayContent(){
        if(isLoading()){
            mLoadingView.setVisibility(View.INVISIBLE);
        }
    }

    public boolean isLoading(){
        return mLoadingView.getVisibility() == View.VISIBLE;
    }
}
