package com.xxx.gogo.view.provider;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.squareup.otto.Subscribe;
import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.BusEvent;
import com.xxx.gogo.MainActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.shopcart.ShopCartEvent;
import com.xxx.gogo.model.goods.GoodsCategoryModel;
import com.xxx.gogo.model.goods.GoodsCategoryModelFactory;
import com.xxx.gogo.model.provider.ProviderItemInfo;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.utils.CommonUtils;


public class ProviderDetailActivity extends BaseToolBarActivity
        implements View.OnClickListener,
        GoodsCategoryModel.Callback{
    private static final int VIEW_LOADING = 0;
    private static final int VIEW_LOAD_AGAIN = 1;
    private static final int VIEW_GRID = 2;

    private TextView mShopCartCountTv;
    private TextView mTotalValueTv;
    private ViewAnimator mViewAnimator;

    private ProviderDetailAdapter mAdapter;
    private GoodsCategoryModel mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_provider_detail);

        createNormalToolBar(R.string.provider_detail, this);
        initView();

        BusFactory.getBus().register(this);
    }

    private void initView(){
        mViewAnimator = (ViewAnimator) findViewById(R.id.view_animator);
        mViewAnimator.setDisplayedChild(VIEW_LOADING);
        mViewAnimator.findViewById(R.id.id_btn_load_again).setOnClickListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.goods_cat_header, null);

        //TODO we should use view to replace this activity later!
        ProviderItemInfo info = ProviderItemDetailModel.getInstance().getItemInfo();
        if(info == null){
            finish();
            return;
        }
        mModel = GoodsCategoryModelFactory.createCategoryModel(this, info.id);
        mModel.load();
        mAdapter = new ProviderDetailAdapter(header, info.id, mModel);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.id_recycler_view);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? layoutManager.getSpanCount() : 1;
            }
        });
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
                if(position == 0){
                    outRect.set(0, 40, 0, 20);
                }else {
                    outRect.set(20, 24, 20, 24);
                }
            }
        });

        TextView id = (TextView) header.findViewById(R.id.id);
        TextView name = (TextView) header.findViewById(R.id.name);
        TextView phone = (TextView) header.findViewById(R.id.phone);

        String strId = getResources().getString(R.string.id);
        strId += info.id;
        id.setText(strId);

        String strName = getResources().getString(R.string.name);
        strName += info.name;
        name.setText(strName);

        String strPhone = getResources().getString(R.string.mobile_phone);
        strPhone += info.phone;
        phone.setText(strPhone);

        findViewById(R.id.shop_cart_img).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);

        mShopCartCountTv = (TextView) findViewById(R.id.shop_count_tv);
        mShopCartCountTv.setText(String.valueOf(ShopCartModel.getInstance().getCount()));

        mTotalValueTv = (TextView) findViewById(R.id.total_value);
        mTotalValueTv.setText(CommonUtils.formatPrice(
                ShopCartModel.getInstance().getTotalPrice()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BusFactory.getBus().unregister(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (v.getId() == R.id.next || v.getId() == R.id.shop_cart_img){
            BusFactory.getBus().post(new BusEvent.TabSwitcher(BusEvent.TabSwitcher.TAB_SHOPCART));
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.id_btn_load_again){
            mModel.load();
            mViewAnimator.setDisplayedChild(VIEW_LOADING);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if(event instanceof ShopCartEvent.ShopCartDataChanged){
            mShopCartCountTv.setText(String.valueOf(ShopCartModel.getInstance().getCount()));
            mTotalValueTv.setText(CommonUtils.formatPrice(
                    ShopCartModel.getInstance().getTotalPrice()));
        }
    }

    @Override
    public void onLoadFail() {
        mViewAnimator.setDisplayedChild(VIEW_LOAD_AGAIN);
    }

    @Override
    public void onLoadSuccess() {
        if(mViewAnimator.getDisplayedChild() != VIEW_GRID){
            mViewAnimator.setDisplayedChild(VIEW_GRID);
        }
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }
}
