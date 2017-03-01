package com.xxx.gogo.view.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ViewAnimator;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.otto.Subscribe;
import com.xxx.gogo.MainApplication;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.shopcart.ShopCartEvent;
import com.xxx.gogo.model.PagedModel;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.goods.GoodsNetDataSource;
import com.xxx.gogo.utils.Constants;

import java.io.File;
import java.util.List;

public class GoodsFragment extends Fragment implements View.OnClickListener{
    private static final String GOODS_DIR = "goods_dir";
    private static final int COUNT_PER_PAGE = 20;

    private static final int FOOTER_LOAD_MORE = 0;
    private static final int FOOTER_LOADING = 1;
    private static final int FOOTER_NO_MORE = 2;

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_FAIL = 1;
    private static final int VIEW_CONTENT = 2;
    private static final int VIEW_EMPTY = 3;

    private String mParentCatId;
    private String mCategoryId;
    private String mProviderId;

    private ViewAnimator mFooter;
    private ViewAnimator mViewAnimator;
    private GoodsListAdapter mAdapter;

    private PagedModel<GoodsItemInfo> mPagedModel;

    private GoodsSubCategoryActivity mActivity;

    //TODO fix it later
    public void setActivity(GoodsSubCategoryActivity activity){
        mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null){
            mProviderId = bundle.getString(Constants.KEY_PROVIDER_ID);
            mParentCatId = bundle.getString(Constants.KEY_GOODS_PARENT_CATEGORY_ID);
            mCategoryId = bundle.getString(Constants.KEY_GOODS_CATEGORY_ID);
        }
        if(mPagedModel == null){
            GoodsNetDataSource dataSource = new GoodsNetDataSource(
                    mProviderId, mParentCatId, mCategoryId);
            mPagedModel = new PagedModel<>(makePath(), COUNT_PER_PAGE, dataSource,
                    new TypeToken<List<GoodsItemInfo>>(){}.getType());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewAnimator = (ViewAnimator) inflater.inflate(R.layout.goods_list, container, false);
        if(!mActivity.isLoading()){
            mViewAnimator.setDisplayedChild(VIEW_LOADING);
        }else {
            mViewAnimator.setDisplayedChild(VIEW_EMPTY);
        }
        mViewAnimator.findViewById(R.id.id_btn_load_again).setOnClickListener(this);
        final PullToRefreshListView listView = (PullToRefreshListView) mViewAnimator.findViewById(R.id.list_view);

        listView.setMode(PullToRefreshBase.Mode.DISABLED);

        mAdapter = new GoodsListAdapter(getContext(), mPagedModel);
        listView.setAdapter(mAdapter);

        mFooter = (ViewAnimator) inflater.inflate(R.layout.list_view_footer_loading, null);
        mFooter.setOnClickListener(this);
        mFooter.setDisplayedChild(FOOTER_LOAD_MORE);
        listView.getRefreshableView().addFooterView(mFooter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (listView.getRefreshableView().getLastVisiblePosition() >= view.getCount() - 1) {
                    waitForLoadingMoreData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        BusFactory.getBus().register(this);

        tryLoadData();

        return mViewAnimator;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPagedModel.destroy();

        BusFactory.getBus().unregister(this);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if(event instanceof ShopCartEvent.ShopCartDataChanged){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.id_btn_load_again){
            mViewAnimator.setDisplayedChild(VIEW_LOADING);
            tryLoadData();
        }else if (v.getId() == R.id.id_footer){
            if(mFooter.getDisplayedChild() == FOOTER_LOAD_MORE){
                waitForLoadingMoreData();
            }
        }
    }

    private String makePath(){
        return mProviderId + File.separator
                + mParentCatId + File.separator + mCategoryId;
    }

    private void tryLoadData(){
        mPagedModel.loadNext(new PagedModel.Callback() {
            @Override
            public void onLoadSuccess() {
                mActivity.displayContent();

                if(mViewAnimator.getDisplayedChild() != VIEW_CONTENT){
                    mViewAnimator.setDisplayedChild(VIEW_CONTENT);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoadFail() {
                if(mPagedModel.getCount() == 0){
                    mActivity.displayContent();
                    mViewAnimator.setDisplayedChild(VIEW_FAIL);
                }else {
                    mFooter.setDisplayedChild(FOOTER_LOAD_MORE);
                }
            }

            @Override
            public void onHasNoData() {
                mFooter.setDisplayedChild(FOOTER_NO_MORE);
            }
        });
    }

    private void waitForLoadingMoreData(){
        if(mFooter.getDisplayedChild() != FOOTER_NO_MORE){
            mFooter.setDisplayedChild(FOOTER_LOADING);
            tryLoadData();
        }
    }
}
