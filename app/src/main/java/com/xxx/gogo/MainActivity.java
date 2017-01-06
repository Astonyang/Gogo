package com.xxx.gogo;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxx.gogo.net.VolleyWrapper;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.ThreadManager;
import com.xxx.gogo.view.provider.SearchProviderActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThreadManager.start();
        VolleyWrapper.getInstance().init(this);

        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowCustomEnabled(true);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View view = LayoutInflater.from(this).inflate(R.layout.toolbar_main, null);
            actionBar.setCustomView(view, layoutParams);

            view.findViewById(R.id.add).setOnClickListener(this);
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        final MainFragmentAdapter adapter = new MainFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); ++i){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(tab != null){
                tab.setIcon(adapter.getResourceId(i, false));
            }
        }
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        if(tab != null){
            tab.select();
            tab.setIcon(adapter.getResourceId(1, true));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private TabLayout.Tab mPreTab;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(adapter.getResourceId(tab.getPosition(), true));
                mPreTab.setIcon(adapter.getResourceId(mPreTab.getPosition(), false));
                viewPager.setCurrentItem(tab.getPosition(), false);
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
        ThreadManager.stop();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SearchProviderActivity.class);
        startActivityForResult(intent, Constants.START_SEARCH_PROVIDER_CODE);
        overridePendingTransition(0, 0);
    }
}
