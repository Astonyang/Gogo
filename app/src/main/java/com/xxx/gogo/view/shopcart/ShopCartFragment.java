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
import com.xxx.gogo.model.provider.ProviderModel;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.StartupMetrics;
import com.xxx.gogo.view.order.OrderConfirmActivity;
import com.xxx.gogo.view.user.LoginActivity;
import com.xxx.gogo.R;

import static android.app.Activity.RESULT_OK;

public class ShopCartFragment extends Fragment
        implements View.OnClickListener,
        ShopCartModel.Callback,
        PullToRefreshBase.OnRefreshListener,
        PullToRefreshBase.OnLastItemVisibleListener{

    private static final int NOT_LOGIN_VIEW = 0;
    private static final int LOADING_VIEW = 1;
    private static final int GO_SHOPPPING = 2;
    private static final int LIST_VIEW = 3;

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

        mContainer = (ViewFlipper) inflater.inflate(R.layout.shop_cart, container, false);
        mContainer.setDisplayedChild(LIST_VIEW);

        mTotalValueTextView = (TextView) mContainer.findViewById(R.id.total_value);
        mDiv = mContainer.findViewById(R.id.div);
        mNextBtn = mContainer.findViewById(R.id.next_container);

        mContainer.findViewById(R.id.next_btn).setOnClickListener(this);
        mContainer.findViewById(R.id.login_btn).setOnClickListener(this);
        mContainer.findViewById(R.id.clear_btn).setOnClickListener(this);

        mPullRefreshListView = (PullToRefreshListView)mContainer.findViewById(
                R.id.pull_refresh_list);

        ShopCartModel.getInstance().setCallback(this);
        mAdapter = new ShopCartListViewAdapter(getContext());
        mPullRefreshListView.setAdapter(mAdapter);

        return mContainer;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_btn){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, Constants.START_LOGIN_FROM_SHOPCART);
        }else if(v.getId() == R.id.next_btn){
            Intent intent = new Intent(getContext(), OrderConfirmActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.clear_btn){
            ShopCartModel.getInstance().clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.START_LOGIN_FROM_SHOPCART && resultCode == RESULT_OK){
            mContainer.setDisplayedChild(LOADING_VIEW);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onLastItemVisible() {

    }

    @Override
    public void onShopCartChanged() {
        mAdapter.notifyDataSetChanged();
        if(ProviderModel.getInstance().getCount() != 0){
            showNextView();
        }else if(mDiv.getVisibility() == View.VISIBLE) {
            mDiv.setVisibility(View.GONE);
            mNextBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFail() {

    }

    @Override
    public void onSuccess() {
        mAdapter.notifyDataSetChanged();
        showNextView();
    }

    private void showNextView(){
        if(mDiv.getVisibility() == View.GONE){
            mDiv.setVisibility(View.VISIBLE);
            mNextBtn.setVisibility(View.VISIBLE);
        }
    }
}
