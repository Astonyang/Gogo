package com.xxx.gogo.view.provider;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewSwitcher;

import com.xxx.gogo.R;
import com.xxx.gogo.model.provider.ProviderSearcher;
import com.xxx.gogo.utils.CommonUtils;
import com.xxx.gogo.utils.ThreadManager;
import com.xxx.gogo.utils.ToastManager;

public class ProviderSearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener,
        View.OnClickListener,
        ProviderSearcher.Callback{

    private static final int FOOTER_LOAD_MORE = 0;
    private static final int FOOTER_LOADING = 1;
    private static final int FOOTER_NO_MORE = 2;

    private TextView mSearchTip;
    private SearchView mSearchView;
    private ViewSwitcher mViewSwitcher;
    private View mDiv1;
    private ViewAnimator mFooter;
    private ListView mListView;

    private ProviderSearcher mDataSource;
    private ProviderSearchResultAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_searcher);

        mDiv1 = findViewById(R.id.div1);

        mSearchTip = (TextView) findViewById(R.id.search_tip);
        mSearchTip.setOnClickListener(this);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);

        mSearchView.setOnQueryTextListener(this);

        mDataSource = new ProviderSearcher(this);

        mFooter = (ViewAnimator) LayoutInflater.from(this).inflate(
                R.layout.list_view_footer_loading, null);

        mListView = (ListView) findViewById(R.id.contact_list_view);
        mAdapter = new ProviderSearchResultAdapter(this, mDataSource);

        mFooter.setOnClickListener(this);
        mFooter.setDisplayedChild(FOOTER_LOAD_MORE);
        mListView.addFooterView(mFooter);
        mListView.setAdapter(mAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mListView.getLastVisiblePosition() == view.getCount() - 1) {

                    waitForLoadingMoreData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                CommonUtils.toggleSoftInput(ProviderSearchActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.search_tip){
            doSearch(mSearchTip.getText().toString());
        }else if (v.getId() == R.id.id_footer){
            if(mFooter.getDisplayedChild() == FOOTER_LOAD_MORE){
                waitForLoadingMoreData();
            }
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(mViewSwitcher.getVisibility() == View.VISIBLE){
            mViewSwitcher.setVisibility(View.GONE);
            mDiv1.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(newText)){
            String str = getResources().getString(R.string.search) + newText;
            if(mDiv1.getVisibility() == View.GONE){
                mDiv1.setVisibility(View.VISIBLE);
            }
            mSearchTip.setText(str);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        return true;
    }

    private void doSearch(String query){
        if(mViewSwitcher.getVisibility() == View.GONE){
            mViewSwitcher.setVisibility(View.VISIBLE);
            mDiv1.setVisibility(View.VISIBLE);
        }
        mViewSwitcher.setDisplayedChild(0);

        mDataSource.load(query);

        CommonUtils.hideInputMethod(mSearchView);
        mSearchView.clearFocus();
    }

    @Override
    public void onFail() {
        ToastManager.showToast(this, getString(R.string.search_provider_no_result));
    }

    @Override
    public void onSuccess(int page) {
        mAdapter.notifyDataSetChanged();
        if(page == 0){
            mViewSwitcher.setDisplayedChild(1);
        }
    }

    private void waitForLoadingMoreData(){
        mFooter.setDisplayedChild(FOOTER_LOADING);

        mDataSource.loadNext();
    }
}
