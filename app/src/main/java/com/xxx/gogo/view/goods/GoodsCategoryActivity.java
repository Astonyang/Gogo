package com.xxx.gogo.view.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.utils.Constants;

public class GoodsCategoryActivity extends BaseToolBarActivity implements View.OnClickListener{
    private GoodsCategoryAdapter mAdapter;
    private String mProviderId;
    private String mGoodsCategoryId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goods_detail);

        Intent intent = getIntent();
        if(intent != null){
            mProviderId = intent.getStringExtra(Constants.KEY_PROVIDER_ID);
            mGoodsCategoryId = intent.getStringExtra(Constants.KEY_GOODS_CATEGORY_ID);
        }

        createNormalToolBar(R.string.goods_list, this);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView(){
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        mAdapter = new GoodsCategoryAdapter(getSupportFragmentManager(), mProviderId);
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
        }
    }
}
