package com.xxx.gogo.view.order;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.BusEvent;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.order.OrderManager;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.often_buy.OftenBuyModel;
import com.xxx.gogo.model.order.OrderConfirmModel;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.model.store_mgr.StoreInfoModel;
import com.xxx.gogo.utils.CommonUtils;
import com.xxx.gogo.utils.DialogHelper;
import com.xxx.gogo.utils.LogUtil;

public class OrderConfirmActivity extends BaseToolBarActivity
        implements View.OnClickListener, OrderConfirmModel.Callback{
    private OrderConfirmModel mModel;
    private OrderConfirmAdapter mAdapter;

    private Dialog mLoadingDialog;

    private ExpandableListView mListView;

    private boolean mCommitSuccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_confirm);

        createNormalToolBar(R.string.order_confirm, this);

        findViewById(R.id.clear_btn).setVisibility(View.GONE);
        findViewById(R.id.next_btn).setOnClickListener(this);

        mListView = (ExpandableListView) findViewById(R.id.list_view);

        mModel = ShopCartModel.getInstance().createOrderConfirmModel();
        mModel.setCallback(this);
        mAdapter = new OrderConfirmAdapter(this, mModel);
        mListView.setAdapter(mAdapter);

        mModel.build();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (v.getId() == R.id.next_btn){
            mLoadingDialog = DialogHelper.showLoadingDialog(this, getString(R.string.processing));
            mLoadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(mCommitSuccess){
                        Dialog dialogInfo = DialogHelper.showDialog(OrderConfirmActivity.this,
                                getString(R.string.order_commit_success));

                        dialogInfo.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(mCommitSuccess){
                                    BusFactory.getBus().post(new BusEvent.TabSwitcher(
                                            BusEvent.TabSwitcher.TAB_PROVIDER));
                                    finish();
                                }
                            }
                        });

                    }else {
                        DialogHelper.showDialog(OrderConfirmActivity.this,
                                getString(R.string.order_commit_fail));
                    }
                }
            });

            OrderManager.getInstance().commitOrder(mModel.getGoodsMap(),
                    new OrderManager.CommitCallback() {
                @Override
                public void onCommitSuccess() {
                    LogUtil.v("commit order succeed order_id = ");

                    mCommitSuccess = true;
                    OftenBuyModel.getInstance().add(mModel.getGoods());
                    ShopCartModel.getInstance().clear();

                    mLoadingDialog.dismiss();
                }

                @Override
                public void onCommitFail() {
                    LogUtil.v("commit order failed order_id = ");

                    mCommitSuccess = false;

                    //TODO for test, remove later!!
                    mModel.modifyState(0, 0, GoodsItemInfo.STATE_UNDERCARRIAGE);
                    //todo end

                    mLoadingDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onDataChanged() {
        if(mModel.getCount() == 0){
            finish();
            return;
        }
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onReady() {
        int count = mAdapter.getGroupCount();
        for (int i = 0; i < count; ++i){
            mListView.expandGroup(i);
        }
        View header = LayoutInflater.from(this).inflate(R.layout.order_confirm_header, mListView, false);
        mListView.addHeaderView(header);

        TextView orderNumTv = (TextView) findViewById(R.id.order_num_tv);
        orderNumTv.setText(String.valueOf(mAdapter.getGroupCount()));

        TextView totalValue = (TextView) findViewById(R.id.total_value);
        totalValue.setText(CommonUtils.formatPrice(
                ShopCartModel.getInstance().getTotalPrice()));

        StoreInfoModel.StoreInfo storeInfo = StoreInfoModel.getInstance().getInfo();
        ((TextView) header.findViewById(R.id.id_name)).setText(storeInfo.name);
        ((TextView) header.findViewById(R.id.id_addr)).setText(storeInfo.addr);
        ((TextView) header.findViewById(R.id.id_owner)).setText(storeInfo.owner);
        ((TextView) header.findViewById(R.id.id_phone)).setText(storeInfo.phone);

        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }
}
