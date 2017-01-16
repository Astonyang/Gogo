package com.xxx.gogo.view.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;

public class GoodsDetailActivity extends BaseToolBarActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goods_detail);

        createNormalToolBar(R.string.goods_detail, this);

        initView();
    }

    private void initView(){
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
