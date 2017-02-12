package com.xxx.gogo.view.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xxx.gogo.MainApplication;
import com.xxx.gogo.R;
import com.xxx.gogo.model.order.OrderModel;
import com.xxx.gogo.utils.Constants;

public class OrderListFragment extends Fragment implements OrderModel.Callback{
    private int mType;
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
        OrderModel.getInstance().setCallback(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.order_list, container, false);
        ListView listView = (ListView) root.findViewById(R.id.list_view);
        mAdapter = new OrderListAdapter(getActivity(), mType);
        listView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onAddFail() {

    }

    @Override
    public void onAddSuccess() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaded() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOrderChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
