package com.xxx.gogo.view.provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.xxx.gogo.R;
import com.xxx.gogo.view.goods.GoodsCategoryAdapter;
import com.xxx.gogo.model.goods.GoodsCategoryModel;
import com.xxx.gogo.model.provider.ProviderItemInfo;
import com.xxx.gogo.model.provider.ProviderModel;


public class ProviderDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private ProviderItemInfo mInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        mInfo = ProviderModel.getInstance().getProviderInfo(id);

        setContentView(R.layout.activity_provider_detail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.provider_detail_toolbar);
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
            View view = LayoutInflater.from(this).inflate(R.layout.toolbar_provider_detail, null);
            actionBar.setCustomView(view, layoutParams);

            View imgView = view.findViewById(R.id.bar_back);
            imgView.setOnClickListener(this);
        }

        initProviderInfoView();
        initCategoryView();
    }

    private void initProviderInfoView(){
        TextView id = (TextView) findViewById(R.id.id);
        TextView name = (TextView) findViewById(R.id.name);
        TextView phone = (TextView) findViewById(R.id.phone);

        String strId = getResources().getString(R.string.id);
        strId += mInfo.id;
        id.setText(strId);

        String strName = getResources().getString(R.string.name);
        strName += mInfo.name;
        name.setText(strName);

        String strPhone = getResources().getString(R.string.mobile_phone);
        strPhone += mInfo.phone;
        phone.setText(strPhone);
    }

    private void initCategoryView(){
        GoodsCategoryModel model = new GoodsCategoryModel();
        GoodsCategoryAdapter adapter = new GoodsCategoryAdapter(this, model);
        GridView gridView = (GridView) findViewById(R.id.goods_grid_view);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }
    }
}
