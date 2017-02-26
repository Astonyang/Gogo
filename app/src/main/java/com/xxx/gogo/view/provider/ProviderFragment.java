package com.xxx.gogo.view.provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

import com.squareup.otto.Subscribe;
import com.xxx.gogo.MainApplication;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.user.UserEvent;
import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.model.BaseModel;
import com.xxx.gogo.model.provider.ProviderModel;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.StartupMetrics;
import com.xxx.gogo.utils.ThreadManager;
import com.xxx.gogo.view.user.LoginActivity;

public class ProviderFragment extends Fragment implements ProviderModel.Callback,
        View.OnClickListener{
    private static final int NOT_LOGIN_VIEW = 0;
    //private static final int LOADING_VIEW = 1;
    private static final int ADD_PROVIDER_VIEW = 1;
    private static final int LIST_VIEW = 2;

    private ProviderAdapter mAdapter;
    private ListView mListView;
    private ViewAnimator mRootContainer;
    private ViewFlipper mContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        StartupMetrics.Log("ProviderFragment::onCreateView");

        mRootContainer = (ViewAnimator) inflater.inflate(R.layout.provider, container, false);

        initToolBar(mRootContainer);

        mContainer = (ViewFlipper) mRootContainer.findViewById(R.id.id_container);
        mContainer.findViewById(R.id.add_provider_tip).setOnClickListener(this);
        mContainer.findViewById(R.id.login_btn).setOnClickListener(this);

        mAdapter = new ProviderAdapter(getContext());
        ProviderModel.getInstance().setCallback(this);

        mListView = (ListView) mRootContainer.findViewById(R.id.contact_list_view);
        mListView.setAdapter(mAdapter);

        if(UserManager.getInstance().isLogin()){
            if(ProviderModel.getInstance().getState() == BaseModel.STATE_LOADING){
                mContainer.setDisplayedChild(LIST_VIEW);
            }else if (ProviderModel.getInstance().getState() == BaseModel.STATE_LOADED){
                if(ProviderModel.getInstance().getCount() == 0){
                    mContainer.setDisplayedChild(ADD_PROVIDER_VIEW);
                }else {
                    mContainer.setDisplayedChild(LIST_VIEW);
                }
            }else if (ProviderModel.getInstance().getState() == BaseModel.STATE_INIT){
                ProviderModel.getInstance().load();
                mContainer.setDisplayedChild(LIST_VIEW);
            }
        }else {
            mContainer.setDisplayedChild(NOT_LOGIN_VIEW);
        }

        BusFactory.getBus().register(this);

        return mRootContainer;
    }

    private void initToolBar(View root){
        TextView titleView = (TextView) root.findViewById(R.id.id_title);
        titleView.setText(getString(R.string.provider));
        View btnContainer = root.findViewById(R.id.id_btn_container);
        btnContainer.setVisibility(View.VISIBLE);
        btnContainer.findViewById(R.id.add).setOnClickListener(this);
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
        if(v.getId() == R.id.add_provider_tip){
            Intent intent = new Intent(getContext(), ProviderSearchActivity.class);
            startActivityForResult(intent, Constants.START_SEARCH_PROVIDER_CODE);
            getActivity().overridePendingTransition(0, 0);
        }else if (v.getId() == R.id.login_btn){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.add){
            Intent intent = new Intent(getContext(), ProviderSearchActivity.class);
            startActivityForResult(intent, Constants.START_SEARCH_PROVIDER_CODE);
            getActivity().overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onLoadSuccess() {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        mAdapter.notifyDataSetChanged();

        if(ProviderModel.getInstance().getCount() != 0){
            mContainer.setDisplayedChild(LIST_VIEW);
        }else {
            mContainer.setDisplayedChild(ADD_PROVIDER_VIEW);
        }
    }

    @Override
    public void onLoadFail() {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(ProviderModel.getInstance().getCount() == 0){
            mContainer.setDisplayedChild(ADD_PROVIDER_VIEW);
        }
    }

    @Override
    public void onAddItem() {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mContainer.getDisplayedChild() != LIST_VIEW){
            mContainer.setDisplayedChild(LIST_VIEW);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteItem() {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(ProviderModel.getInstance().getCount() == 0){
            mContainer.setDisplayedChild(ADD_PROVIDER_VIEW);
        }
        mAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if (event instanceof UserEvent.UserLoginSuccess){
            mContainer.setDisplayedChild(LIST_VIEW);
            ProviderModel.getInstance().load();
        }else if (event instanceof UserEvent.UserLogout){
            mContainer.setDisplayedChild(NOT_LOGIN_VIEW);
        }
    }
}
