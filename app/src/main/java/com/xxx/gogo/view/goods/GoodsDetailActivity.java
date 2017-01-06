package com.xxx.gogo.view.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.xxx.gogo.R;

public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goods_detail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.goods_detail_toolbar);
        setSupportActionBar(myToolbar);

        initView();
    }

    private void initView(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowCustomEnabled(true);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View view = LayoutInflater.from(this).inflate(R.layout.toolbar_goods_detail, null);
            actionBar.setCustomView(view, layoutParams);

            View imgView = view.findViewById(R.id.bar_back);
            imgView.setOnClickListener(this);
        }

        PullToRefreshExpandableListView listView = (PullToRefreshExpandableListView)
                findViewById(R.id.list_view);
        GoodsSecondaryCategoryAdapter adapter = new GoodsSecondaryCategoryAdapter(this);
        listView.getRefreshableView().setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }
    }
}
