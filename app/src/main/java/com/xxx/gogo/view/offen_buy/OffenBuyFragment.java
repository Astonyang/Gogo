package com.xxx.gogo.view.offen_buy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.StartupMetrics;
import com.xxx.gogo.view.user.LoginActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.model.offen_buy.OffenBuyModel;

import static android.app.Activity.RESULT_OK;

public class OffenBuyFragment extends Fragment
        implements View.OnClickListener,
        PullToRefreshBase.OnRefreshListener,
        PullToRefreshBase.OnLastItemVisibleListener{
    private static final int NOT_LOGIN_VIEW = 0;
    private static final int LOADING_VIEW = 1;
    private static final int LIST_VIEW = 2;

    private OffenBuyView mContainer;
    private PullToRefreshExpandableListView mPullRefreshListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        StartupMetrics.Log("OffenBuyFragment::onCreateView");

        mContainer = (OffenBuyView) inflater.inflate(R.layout.offen_buy, container, false);
        mContainer.setDisplayedChild(LIST_VIEW);

        mContainer.findViewById(R.id.login_btn).setOnClickListener(this);
        mPullRefreshListView = (PullToRefreshExpandableListView) mContainer.findViewById(
                R.id.list_view);
        OffenBuyListViewAdapter adapter = new OffenBuyListViewAdapter(getContext());
        mPullRefreshListView.getRefreshableView().setAdapter(adapter);

        return mContainer;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_btn){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, Constants.START_LOGIN_FROM_FAVO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK){
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
