package com.xxx.gogo.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.MainActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.model.order.OrderModel;
import com.xxx.gogo.utils.Constants;

public class OrderListActivity extends BaseToolBarActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        createNormalToolBar(R.string.order_list, this);

        OrderModel.getInstance().load();

        initViews();
    }

    private void initViews() {
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        final OrderFragmentAdapter adapter = new OrderFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.START_ORDER_DETAIL_ACTIVITY && data != null){
            boolean exit = data.getBooleanExtra(Constants.KEY_EXIT_ACTIVITY, false);
            if(exit){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.KEY_SWITCH_SHOP_CART, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }
    }
}
