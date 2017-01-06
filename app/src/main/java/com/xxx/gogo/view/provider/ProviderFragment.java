package com.xxx.gogo.view.provider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import com.xxx.gogo.R;
import com.xxx.gogo.model.provider.ProviderModel;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.ThreadManager;

public class ProviderFragment extends Fragment implements ProviderModel.Callback,
        View.OnClickListener{
    private static final int LOADING_VIEW = 0;
    private static final int ADD_PROVIDER_VIEW = 1;

    private ProviderAdapter mAdapter;
    private ListView mListView;
    private ViewSwitcher mEmptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout root = (FrameLayout) inflater.inflate(R.layout.provider, container, false);

        mEmptyView = (ViewSwitcher) root.findViewById(R.id.empty);
        mEmptyView.findViewById(R.id.add_provider_tip).setOnClickListener(this);

        mAdapter = new ProviderAdapter(getContext());
        ProviderModel.getInstance().setCallback(this);

        mListView = (ListView) root.findViewById(R.id.contact_list_view);
        mListView.setAdapter(mAdapter);
        mEmptyView.setDisplayedChild(LOADING_VIEW);
        mListView.setEmptyView(mEmptyView);

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_provider_tip){
            Intent intent = new Intent(getContext(), SearchProviderActivity.class);
            startActivityForResult(intent, Constants.START_SEARCH_PROVIDER_CODE);
            getActivity().overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.START_SEARCH_PROVIDER_CODE){

        }
    }

    @Override
    public void onLoadSuccess() {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFail() {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mAdapter.getCount() == 0){
            mEmptyView.setDisplayedChild(ADD_PROVIDER_VIEW);
        }
    }

    @Override
    public void onAddItem() {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteItem() {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mAdapter.getCount() == 0){
            mEmptyView.setDisplayedChild(ADD_PROVIDER_VIEW);
        }
        mAdapter.notifyDataSetChanged();
    }
}
