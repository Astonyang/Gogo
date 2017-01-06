package com.xxx.gogo.view.provider;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.xxx.gogo.R;
import com.xxx.gogo.model.ProviderSearcher;
import com.xxx.gogo.model.provider.ProviderItemInfo;
import com.xxx.gogo.model.provider.ProviderModel;

public class SearchProviderActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener,
        View.OnClickListener , ProviderSearcher.Callback{
    private ViewSwitcher mViewSwitcher;
    private TextView mSearchTip;
    private SearchView mSearchView;
    private View mDiv1;
    private TextView mContactNameTv;

    private ProviderSearcher mDataSource;
    private ProviderItemInfo mInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_provider);

        mDiv1 = findViewById(R.id.div1);
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);

        mSearchTip = (TextView) findViewById(R.id.search_tip);
        mSearchTip.setOnClickListener(this);
        mSearchView = (SearchView) findViewById(R.id.search_view);

        mContactNameTv = (TextView) findViewById(R.id.contact_name);
        findViewById(R.id.add).setOnClickListener(this);

        mSearchView.setOnQueryTextListener(this);

        mDataSource = new ProviderSearcher(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.search_tip){
            doSearch(mSearchTip.getText().toString());
        }else if(v.getId() == R.id.add){
            ProviderModel.getInstance().addItem(mInfo);
            finish();
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
    }

    @Override
    public void onFail() {

    }

    @Override
    public void onSuccess(ProviderItemInfo info) {
        mInfo = info;
        mViewSwitcher.setDisplayedChild(1);
        mContactNameTv.setText(info.name);
    }
}
