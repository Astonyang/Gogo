package com.xxx.gogo.view.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xxx.gogo.R;
import com.xxx.gogo.model.order.AllOrderModel;
import com.xxx.gogo.model.order.CompletedOrderModel;
import com.xxx.gogo.model.order.PendingOrderModel;

public class OrderListFragment extends Fragment implements AllOrderModel.Callback{
    public static final int TYPE_ALL_ORDER = 0;
    public static final int TYPE_PENDING_ORDER = 1;
    public static final int TYPE_COMPLETED_ORDER = 2;

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
            mType = bundle.getInt("type");
        }
        switch (mType){
            case TYPE_ALL_ORDER:
                AllOrderModel.getInstance().setCallback(this);
                break;
            case TYPE_COMPLETED_ORDER:
                CompletedOrderModel.getInstance().setCallback(this);
                break;
            case TYPE_PENDING_ORDER:
                PendingOrderModel.getInstance().setCallback(this);
                break;
            default:
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.order_list, container, false);
        ListView listView = (ListView) root.findViewById(R.id.list_view);
        mAdapter = new OrderListAdapter(getActivity(), mType);
        listView.setAdapter(mAdapter);

        switch (mType){
            case TYPE_ALL_ORDER:
                AllOrderModel.getInstance().load();
                break;
            case TYPE_COMPLETED_ORDER:
                CompletedOrderModel.getInstance().load();
                break;
            case TYPE_PENDING_ORDER:
                PendingOrderModel.getInstance().load();
                break;
            default:
                break;
        }

        return root;
    }

    @Override
    public void onSuccess() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOrderChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFail() {

    }
}
