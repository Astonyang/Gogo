package com.xxx.gogo.view.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.otto.Subscribe;
import com.xxx.gogo.MainApplication;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.order.OrderEvent;
import com.xxx.gogo.manager.order.OrderManager;
import com.xxx.gogo.utils.Constants;

public class OrderListFragment extends Fragment
        implements PullToRefreshBase.OnRefreshListener<ListView>{
    private int mType;
    private PullToRefreshListView mListView;
    private OrderListAdapter mAdapter;

    public OrderListFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            mType = bundle.getInt(Constants.KEY_ORDER_TYPE);
        }
        BusFactory.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BusFactory.getBus().unregister(this);
        MainApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.order_list, container, false);
        mListView = (PullToRefreshListView) root.findViewById(R.id.list_view);
        mListView.setOnRefreshListener(this);

        mAdapter = new OrderListAdapter(getActivity(), mType);
        mListView.setAdapter(mAdapter);

        return root;
    }

    public void notifyOrderLoaded(){
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        mListView.setRefreshing(true);
        OrderManager.getInstance().checkOrderState();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if(event instanceof OrderEvent.CheckOrderStateComplete){
            if(mAdapter != null){
                mAdapter.notifyDataSetChanged();
                if(mListView.isRefreshing()){
                    mListView.onRefreshComplete();
                }
            }
        }
    }
}
