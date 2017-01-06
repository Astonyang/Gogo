package com.xxx.gogo.view.shopcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.view.user.LoginActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.model.shopcart.ShopCartModel;

import static android.app.Activity.RESULT_OK;

public class ShopCartFragment extends Fragment
        implements View.OnClickListener,
        PullToRefreshBase.OnRefreshListener,
        PullToRefreshBase.OnLastItemVisibleListener{

    private static final int NOT_LOGIN_VIEW = 0;
    private static final int LOADING_VIEW = 1;
    private static final int LIST_VIEW = 2;

    private ShopCartView mContainer;
    private PullToRefreshListView mPullRefreshListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mContainer = (ShopCartView) inflater.inflate(R.layout.shop_cart, container, false);
        mContainer.setDisplayedChild(NOT_LOGIN_VIEW);

        mContainer.findViewById(R.id.login_btn).setOnClickListener(this);
        mPullRefreshListView = (PullToRefreshListView)mContainer.findViewById(
                R.id.pull_refresh_list);
        ShopCartListViewAdapter adapter = new ShopCartListViewAdapter(getContext());
        mPullRefreshListView.setAdapter(adapter);

        return mContainer;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent, Constants.START_LOGIN_FROM_SHOPCART);
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
}
