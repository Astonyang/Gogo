package com.xxx.gogo;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.StartupMetrics;
import com.xxx.gogo.view.provider.SearchProviderActivity;

public class MainActivity extends BaseToolBarActivity implements View.OnClickListener{
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StartupMetrics.Log("before MainActivity::onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        createToolBar();

        initViews();

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
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mPager = (ViewPager) findViewById(R.id.viewPager);

        final MainFragmentAdapter adapter = new MainFragmentAdapter(this, getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(mPager);
        for (int i = 0; i < tabLayout.getTabCount(); ++i){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(tab != null){
                tab.setIcon(adapter.getResourceId(i, false));
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private TabLayout.Tab mPreTab;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(adapter.getResourceId(tab.getPosition(), true));
                mPreTab.setIcon(adapter.getResourceId(mPreTab.getPosition(), false));
                mPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mPreTab = tab;
                tab.setIcon(adapter.getResourceId(tab.getPosition(), false));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_container){
            Intent intent = new Intent(this, SearchProviderActivity.class);
            startActivityForResult(intent, Constants.START_SEARCH_PROVIDER_CODE);
            overridePendingTransition(0, 0);
        }
    }

    public void switchPage(int pos){
        mPager.setCurrentItem(pos, false);
    }
}
