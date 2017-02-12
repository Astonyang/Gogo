package com.xxx.gogo.view.often_buy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.otto.Subscribe;
import com.xxx.gogo.BusEvent;
import com.xxx.gogo.MainApplication;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.shopcart.ShopCartEvent;
import com.xxx.gogo.manager.user.UserEvent;
import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.model.BaseModel;
import com.xxx.gogo.model.often_buy.OftenBuyModel;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.StartupMetrics;
import com.xxx.gogo.view.user.LoginActivity;
import com.xxx.gogo.R;

public class OftenBuyFragment extends Fragment
        implements View.OnClickListener,
        PullToRefreshBase.OnRefreshListener,
        OftenBuyModel.Callback,
        PullToRefreshBase.OnLastItemVisibleListener{

    private static final int NOT_LOGIN_VIEW = 0;
    private static final int LOADING_VIEW = 1;
    private static final int LIST_VIEW = 2;

    private OftenBuyView mContainer;
    private PullToRefreshListView mPullRefreshListView;
    private OftenBuyListViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        StartupMetrics.Log("OftenBuyFragment::onCreateView");

        View root = inflater.inflate(R.layout.offen_buy, container, false);

        initToolBar(root);

        mContainer = (OftenBuyView) root.findViewById(R.id.id_offen_buy);

        mContainer.findViewById(R.id.login_btn).setOnClickListener(this);
        mPullRefreshListView = (PullToRefreshListView) mContainer.findViewById(
                R.id.list_view);

        mAdapter = new OftenBuyListViewAdapter(getActivity());
        mPullRefreshListView.getRefreshableView().setAdapter(mAdapter);

        OftenBuyModel.getInstance().setCallback(this);

        if(UserManager.getInstance().isLogin()){
            if(OftenBuyModel.getInstance().getState() == BaseModel.STATE_LOADING){
                mContainer.setDisplayedChild(LOADING_VIEW);
            }else if (OftenBuyModel.getInstance().getState() == BaseModel.STATE_LOADED){
                if(OftenBuyModel.getInstance().getCount() == 0){
                    //// TODO: 17/2/10
                    //mContainer.setDisplayedChild();
                }else {
                    mContainer.setDisplayedChild(LIST_VIEW);
                }
            }else if (OftenBuyModel.getInstance().getState() == BaseModel.STATE_INIT){
                OftenBuyModel.getInstance().load();
                mContainer.setDisplayedChild(LOADING_VIEW);
            }
        }else {
            mContainer.setDisplayedChild(NOT_LOGIN_VIEW);
        }

        BusFactory.getBus().register(this);

        return root;
    }

    private void initToolBar(View root){
        //View toolbar = root.findViewById(R.id.my_toolbar);
        TextView titleView = (TextView) root.findViewById(R.id.id_title);
        titleView.setText(getString(R.string.favo));
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
        if(v.getId() == R.id.login_btn){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, Constants.START_LOGIN_FROM_FAVO);
        }else if (v.getId() == R.id.next_btn){
            BusFactory.getBus().post(new BusEvent.TabSwitcher(BusEvent.TabSwitcher.TAB_SHOPCART));
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
        if (event instanceof ShopCartEvent.ShopCartDataChanged){
            mAdapter.notifyDataSetChanged();
        }else if (event instanceof UserEvent.UserLoginSuccess){
            mContainer.setDisplayedChild(LOADING_VIEW);
            OftenBuyModel.getInstance().load();
        }else if (event instanceof UserEvent.UserLogout){
            mContainer.setDisplayedChild(NOT_LOGIN_VIEW);
            OftenBuyModel.getInstance().clear();
        }
    }

    @Override
    public void onLoadFail() {
        mContainer.setDisplayedChild(LIST_VIEW);
    }

    @Override
    public void onLoadSuccess() {
        mContainer.setDisplayedChild(LIST_VIEW);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAdded() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRemoved() {
        mAdapter.notifyDataSetChanged();
    }
}
