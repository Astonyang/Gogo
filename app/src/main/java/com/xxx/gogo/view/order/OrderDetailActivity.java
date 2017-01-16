package com.xxx.gogo.view.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.model.order.AllOrderModel;
import com.xxx.gogo.model.order.OrderItemInfo;
import com.xxx.gogo.utils.CommonUtils;
import com.xxx.gogo.utils.Constants;

public class OrderDetailActivity extends BaseToolBarActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_detail);

        createNormalToolBar(R.string.order_detail, this);

        initView();
    }

    private void initView(){
        ListView listView = (ListView) findViewById(R.id.list_view);
        View headerView = LayoutInflater.from(this).inflate(R.layout.order_detail_header, null);
        TextView tvOrderIndex = (TextView) headerView.findViewById(R.id.order_num_value);
        TextView tvState = (TextView) headerView.findViewById(R.id.state);
        TextView tvShopName = (TextView) headerView.findViewById(R.id.shop_name);
        TextView tvOrderPrice = (TextView) headerView.findViewById(R.id.order_total_price_value);
        TextView tvOrderStartTime = (TextView) headerView.findViewById(R.id.order_start_time_value);
        TextView tvGoodsNum = (TextView) headerView.findViewById(R.id.goods_num_value);

        final OrderItemInfo itemInfo = AllOrderModel.getInstance().getDetailItem();
        if(itemInfo == null) {
            finish();
            return;
        }
        tvOrderIndex.setText(itemInfo.id);

        tvState.setBackgroundResource(R.drawable.login_btn_bg);
        tvState.setText(getResources().getString(R.string.make_order_again));
        tvState.setTextColor(Color.parseColor("#ffffff"));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvState.getLayoutParams();
        layoutParams.topMargin = CommonUtils.dp2px(this, 2);
        layoutParams.bottomMargin = CommonUtils.dp2px(this, 2);

        tvShopName.setText(itemInfo.shopName);
        tvOrderPrice.setText(itemInfo.price);
        tvGoodsNum.setText(itemInfo.goodsNum);
        tvOrderStartTime.setText(itemInfo.startTime);

        listView.addHeaderView(headerView);

        OrderDetailListAdapter adapter = new OrderDetailListAdapter(this);
        listView.setAdapter(adapter);

        tvState.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (v.getId() == R.id.state){
            Intent intent = new Intent(this, OrderListActivity.class);
            intent.putExtra(Constants.KEY_EXIT_ACTIVITY, true);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
