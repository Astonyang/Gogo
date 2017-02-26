package com.xxx.gogo.view.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.otto.Subscribe;
import com.xxx.gogo.MainApplication;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.shopcart.ShopCartEvent;
import com.xxx.gogo.utils.Constants;

public class GoodsFragment extends Fragment{
    private String mCategoryId;
    private String mProviderId;
    private GoodsListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null){
            mProviderId = bundle.getString(Constants.KEY_PROVIDER_ID);
            mCategoryId = bundle.getString(Constants.KEY_GOODS_CATEGORY_ID);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BusFactory.getBus().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.goods_list, container, false);
        PullToRefreshListView listView = (PullToRefreshListView) root.findViewById(R.id.list_view);

        listView.setMode(PullToRefreshBase.Mode.DISABLED);

        mAdapter = new GoodsListAdapter(getContext(), mProviderId, mCategoryId);
        listView.setAdapter(mAdapter);

        BusFactory.getBus().register(this);

        return root;
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if(event instanceof ShopCartEvent.ShopCartDataChanged){
            mAdapter.notifyDataSetChanged();
        }
    }
}
