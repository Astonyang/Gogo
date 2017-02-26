package com.xxx.gogo.view.shopcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.otto.Subscribe;
import com.xxx.gogo.BusEvent;
import com.xxx.gogo.MainApplication;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.shopcart.ShopCartEvent;
import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.model.BaseModel;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.utils.CommonUtils;
import com.xxx.gogo.utils.StartupMetrics;
import com.xxx.gogo.view.order.OrderConfirmActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.view.user.LoginActivity;

public class ShopCartFragment extends Fragment
        implements View.OnClickListener,
        PullToRefreshBase.OnRefreshListener,
        PullToRefreshBase.OnLastItemVisibleListener{

    private static final int GO_SHOPPING = 0;
    private static final int LIST_VIEW = 1;
    private static final int LOADING_VIEW = 2;

    private ViewFlipper mContainer;
    private PullToRefreshListView mPullRefreshListView;
    private ShopCartListViewAdapter mAdapter;
    private TextView mTotalValueTextView;
    private View mNextBtn;
    private View mDiv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        StartupMetrics.Log("ShopCartFragment::onCreateView");

        View root = inflater.inflate(R.layout.shop_cart, container, false);

        initToolBar(root);

        mContainer = (ViewFlipper) root.findViewById(R.id.id_flipper);

                mTotalValueTextView = (TextView) mContainer.findViewById(R.id.total_value);
        mTotalValueTextView.setText(CommonUtils.formatPrice(
                ShopCartModel.getInstance().getTotalPrice()));

        mDiv = mContainer.findViewById(R.id.div);
        mNextBtn = mContainer.findViewById(R.id.next_container);

        mContainer.findViewById(R.id.next_btn).setOnClickListener(this);
        mContainer.findViewById(R.id.clear_btn).setOnClickListener(this);
        mContainer.findViewById(R.id.id_go_shopping_btn).setOnClickListener(this);

        mPullRefreshListView = (PullToRefreshListView) mContainer.findViewById(
                R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);

        mAdapter = new ShopCartListViewAdapter(getContext());
        mPullRefreshListView.setAdapter(mAdapter);

        if(ShopCartModel.getInstance().getCount() != 0){
            mContainer.setDisplayedChild(LIST_VIEW);
            showNextView();
        }else if(ShopCartModel.getInstance().getState() == BaseModel.STATE_LOADING) {
            mContainer.setDisplayedChild(LOADING_VIEW);
        }else if (ShopCartModel.getInstance().getState() == BaseModel.STATE_LOADED){
            if(ShopCartModel.getInstance().getCount() == 0){
                mContainer.setDisplayedChild(GO_SHOPPING);
            }
        }

        BusFactory.getBus().register(this);

        return root;
    }

    private void initToolBar(View root){
        //View toolbar = root.findViewById(R.id.my_toolbar);
        TextView titleView = (TextView) root.findViewById(R.id.id_title);
        titleView.setText(getString(R.string.shop_cart));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BusFactory.getBus().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.next_btn){
            if(UserManager.getInstance().isLogin()){
                Intent intent = new Intent(getContext(), OrderConfirmActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
            }
        }else if(v.getId() == R.id.clear_btn){
            ShopCartModel.getInstance().clear();
        }else if (v.getId() == R.id.id_go_shopping_btn){
            BusFactory.getBus().post(new BusEvent.TabSwitcher(BusEvent.TabSwitcher.TAB_PROVIDER));
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onLastItemVisible() {

    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if(event instanceof ShopCartEvent.ShopCartDataLoaded){
            mAdapter.notifyDataSetChanged();
            if(ShopCartModel.getInstance().getCount() != 0){
                mContainer.setDisplayedChild(LIST_VIEW);
                showNextView();
            }else {
                mContainer.setDisplayedChild(GO_SHOPPING);
            }

        }else if (event instanceof ShopCartEvent.ShopCartDataChanged) {
            ShopCartEvent.ShopCartDataChanged changed = (ShopCartEvent.ShopCartDataChanged) event;
            mAdapter.notifyDataSetChanged();

            if (changed.mChangedType != ShopCartEvent.ShopCartDataChanged.TYPE_PRICE_CHANGED) {
                if (ShopCartModel.getInstance().getCount() != 0) {
                    mContainer.setDisplayedChild(LIST_VIEW);
                    showNextView();

                } else if (mDiv.getVisibility() == View.VISIBLE) {
                    mContainer.setDisplayedChild(GO_SHOPPING);
                    mDiv.setVisibility(View.GONE);
                    mNextBtn.setVisibility(View.GONE);
                }
            }
            mTotalValueTextView.setText(CommonUtils.formatPrice(
                    ShopCartModel.getInstance().getTotalPrice()));

        }
    }

    private void showNextView(){
        if(mDiv.getVisibility() == View.GONE){
            mDiv.setVisibility(View.VISIBLE);
            mNextBtn.setVisibility(View.VISIBLE);
        }
    }
}
