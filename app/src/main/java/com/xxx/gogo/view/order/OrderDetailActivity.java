package com.xxx.gogo.view.order;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.order.OrderManager;
import com.xxx.gogo.model.order.OrderItemDetailModel;
import com.xxx.gogo.model.order.OrderItemInfo;
import com.xxx.gogo.model.order.OrderModel;
import com.xxx.gogo.utils.CommonUtils;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.DialogHelper;

public class OrderDetailActivity extends BaseToolBarActivity implements View.OnClickListener{
    private OrderItemInfo mOrderInfo;
    private Button mCancelBtn;
    private TextView mStateTv;
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_detail);

        createNormalToolBar(R.string.order_detail, this);

        initView();
    }

    private void initView(){
        Intent intent = getIntent();
        int position = -1;
        int type = 0;
        if(intent != null){
            position = intent.getIntExtra(Constants.KEY_ORDER_POSITION, -1);
            type = intent.getIntExtra(Constants.KEY_ORDER_TYPE, -1);
        }
        if(position == -1 || type == -1){
            return;
        }

        ListView listView = (ListView) findViewById(R.id.list_view);
        View headerView = LayoutInflater.from(this).inflate(R.layout.order_detail_header, null);
        TextView tvOrderIndex = (TextView) headerView.findViewById(R.id.order_num_value);
        mStateTv = (TextView) headerView.findViewById(R.id.state);
        TextView tvShopName = (TextView) headerView.findViewById(R.id.shop_name);
        TextView tvOrderPrice = (TextView) headerView.findViewById(R.id.order_total_price_value);
        TextView tvOrderStartTime = (TextView) headerView.findViewById(R.id.order_start_time_value);
        TextView tvGoodsNum = (TextView) headerView.findViewById(R.id.goods_num_value);

        mOrderInfo = OrderModel.getInstance().getItem(type, position);
        if(mOrderInfo == null) {
            finish();
            return;
        }
        tvOrderIndex.setText(mOrderInfo.id);

        mStateTv.setText(mOrderInfo.toStringState(this));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mStateTv.getLayoutParams();
        layoutParams.topMargin = CommonUtils.dp2px(this, 2);
        layoutParams.bottomMargin = CommonUtils.dp2px(this, 2);

        tvShopName.setText(mOrderInfo.storeName);
        tvOrderPrice.setText(CommonUtils.formatPrice(mOrderInfo.price));
        tvGoodsNum.setText(String.valueOf(mOrderInfo.goodsNum));
        tvOrderStartTime.setText(String.valueOf(mOrderInfo.startTime));

        mCancelBtn = (Button) headerView.findViewById(R.id.id_cancel_order);
        if(mOrderInfo.state == OrderItemInfo.STATE_CREATED){
            mCancelBtn.setVisibility(View.VISIBLE);
            mCancelBtn.setOnClickListener(this);
        }

        listView.addHeaderView(headerView);

        OrderItemDetailModel model = new OrderItemDetailModel(mOrderInfo);
        final OrderDetailListAdapter adapter = new OrderDetailListAdapter(this, model);
        listView.setAdapter(adapter);
        model.load(new OrderItemDetailModel.Callback() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
            }
        });

        mStateTv.setOnClickListener(this);
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
        }else if (v.getId() == R.id.id_cancel_order){
            mLoadingDialog = DialogHelper.showLoadingDialog(this, getString(R.string.processing));

            OrderManager.getInstance().cancelOrder(mOrderInfo.id, new OrderManager.CancelCallback() {
                @Override
                public void onCancelSuccess() {
                    mCancelBtn.setVisibility(View.GONE);
                    mStateTv.setText(mOrderInfo.toStringState(OrderDetailActivity.this));

                    mLoadingDialog.dismiss();
                }

                @Override
                public void onCancelFail() {
                    mLoadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            DialogHelper.showDialog(OrderDetailActivity.this,
                                    getString(R.string.fail_to_cancel));
                        }
                    });
                    mLoadingDialog.dismiss();
                }
            });
        }
    }
}
