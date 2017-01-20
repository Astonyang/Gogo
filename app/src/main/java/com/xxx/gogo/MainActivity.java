package com.xxx.gogo;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.shopcart.ShopCartEvent;
import com.xxx.gogo.model.MainDatabaseHelper;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.LogUtil;
import com.xxx.gogo.utils.StartupMetrics;
import com.xxx.gogo.view.provider.SearchProviderActivity;

public class MainActivity extends BaseToolBarActivity implements View.OnClickListener{
    private ViewPager mPager;
    private TabLayout mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StartupMetrics.Log("before MainActivity::onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        createToolBar();

        initViews();

        ShopCartModel.getInstance().setDbHelper(MainDatabaseHelper.getDataBaseHelper(this));
        ShopCartModel.getInstance().load();

        BusFactory.getBus().register(this);

        StartupMetrics.Log("after MainActivity::onCreate");
    }

    @Override
    protected void onResume() {
        StartupMetrics.Log("MainActivity::onResume");
        super.onResume();
    }

    @Override
    protected View createToolBarContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.toolbar_main, null);
        view.findViewById(R.id.add_container).setOnClickListener(this);

        return view;
    }

    private void initViews() {
        mTabs = (TabLayout) findViewById(R.id.tabLayout);
        mPager = (ViewPager) findViewById(R.id.viewPager);

        final MainFragmentAdapter adapter = new MainFragmentAdapter(this, getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(1);
        mTabs.setupWithViewPager(mPager);
        for (int i = 0; i < mTabs.getTabCount(); ++i){
            TabLayout.Tab tab = mTabs.getTabAt(i);
            if(tab != null){
                if(i == 0){
                    tab.setCustomView(adapter.getTabView(i, true));
                }else {
                    tab.setCustomView(adapter.getTabView(i, false));
                }
            }
        }
        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setCustomView(null);
                tab.setCustomView(adapter.getTabView(tab.getPosition(), true));

                mPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
                tab.setCustomView(adapter.getTabView(tab.getPosition(), false));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LogUtil.unInit();
        ShopCartModel.getInstance().save();
        BusFactory.getBus().unregister(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_container){
            Intent intent = new Intent(this, SearchProviderActivity.class);
            startActivityForResult(intent, Constants.START_SEARCH_PROVIDER_CODE);
            overridePendingTransition(0, 0);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if(event instanceof BusEvent.TabSwitcher){
            mPager.setCurrentItem(((BusEvent.TabSwitcher)event).mPos, false);

        }else if(event instanceof ShopCartEvent.ShopCartDataChanged ||
                event instanceof ShopCartEvent.ShopCartDataLoaded){

            TabLayout.Tab tab = mTabs.getTabAt(BusEvent.TabSwitcher.TAB_SHOPCART);
            if(tab != null){
                View container = tab.getCustomView();
                if(container != null){
                    TextView tvCount = (TextView) container.findViewById(R.id.id_badge);

                    int count = ShopCartModel.getInstance().getCount();
                    count = count > 99 ? 99 : count;
                    if(count == 0){
                        tvCount.setVisibility(View.INVISIBLE);
                    }else {
                        tvCount.setVisibility(View.VISIBLE);
                    }
                    tvCount.setText(String.valueOf(count));
                }
            }
        }
    }
}
