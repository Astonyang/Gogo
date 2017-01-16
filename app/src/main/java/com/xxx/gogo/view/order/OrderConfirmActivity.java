package com.xxx.gogo.view.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.model.order.OrderConfirmModel;
import com.xxx.gogo.model.shopcart.ShopCartModel;

public class OrderConfirmActivity extends BaseToolBarActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_confirm);
        findViewById(R.id.clear_btn).setVisibility(View.GONE);

        createNormalToolBar(R.string.order_confirm, this);

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.list_view);
        View header = LayoutInflater.from(this).inflate(R.layout.order_confirm_header, listView, false);
        listView.addHeaderView(header);

        OrderConfirmModel model = ShopCartModel.getInstance().createOrderConfirmModel();
        OrderConfirmAdapter adapter = new OrderConfirmAdapter(this, model);
        listView.setAdapter(adapter);

        int count = adapter.getGroupCount();
        for (int i = 0; i < count; ++i){
            listView.expandGroup(i);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }
    }
}
