package com.xxx.gogo.view.order;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.order.OrderManager;
import com.xxx.gogo.model.often_buy.OftenBuyModel;
import com.xxx.gogo.model.order.OrderConfirmModel;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.utils.CommonUtils;
import com.xxx.gogo.utils.DialogHelper;
import com.xxx.gogo.utils.LogUtil;

public class OrderConfirmActivity extends BaseToolBarActivity implements View.OnClickListener{
    private OrderConfirmModel mModel;
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_confirm);

        createNormalToolBar(R.string.order_confirm, this);

        findViewById(R.id.clear_btn).setVisibility(View.GONE);
        findViewById(R.id.next_btn).setOnClickListener(this);

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.list_view);
        View header = LayoutInflater.from(this).inflate(R.layout.order_confirm_header, listView, false);
        listView.addHeaderView(header);

        mModel = ShopCartModel.getInstance().createOrderConfirmModel();
        OrderConfirmAdapter adapter = new OrderConfirmAdapter(this, mModel);
        listView.setAdapter(adapter);

        int count = adapter.getGroupCount();
        for (int i = 0; i < count; ++i){
            listView.expandGroup(i);
        }
        TextView orderNumTv = (TextView) findViewById(R.id.order_num_tv);
        orderNumTv.setText(String.valueOf(adapter.getGroupCount()));

        TextView totalValue = (TextView) findViewById(R.id.total_value);
        totalValue.setText(CommonUtils.formatPrice(
                ShopCartModel.getInstance().getTotalPrice()));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (v.getId() == R.id.next_btn){
            mLoadingDialog = DialogHelper.createLoadingDialog(this);
            mLoadingDialog.show();

            OrderManager.getInstance().commitOrder(mModel.getGoodsMap(),
                    new OrderManager.CommitCallback() {
                @Override
                public void onCommitSuccess() {
                    LogUtil.v("commit order succeed order_id = ");

                    OftenBuyModel.getInstance().add(mModel.getGoods());
                    ShopCartModel.getInstance().clear();

                    mLoadingDialog.dismiss();
                    finish();
                }

                @Override
                public void onCommitFail() {
                    LogUtil.v("commit order failed order_id = ");

                    mLoadingDialog.dismiss();
                }
            });
        }
    }
}
