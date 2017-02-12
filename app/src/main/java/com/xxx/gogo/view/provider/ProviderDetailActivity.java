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

import com.squareup.otto.Subscribe;
import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.BusEvent;
import com.xxx.gogo.MainActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.shopcart.ShopCartEvent;
import com.xxx.gogo.model.provider.ProviderItemInfo;
import com.xxx.gogo.model.shopcart.ShopCartModel;
import com.xxx.gogo.utils.CommonUtils;


public class ProviderDetailActivity extends BaseToolBarActivity implements View.OnClickListener{
    private TextView mShopCartCountTv;
    private TextView mTotalValueTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_provider_detail2);

        createNormalToolBar(R.string.provider_detail, this);
        initView();

        BusFactory.getBus().register(this);
    }

    private void initView(){
        View header = LayoutInflater.from(this).inflate(R.layout.goods_cat_header, null);

        ProviderItemInfo info = ProviderItemDetailModel.getInstance().getItemInfo();
        ProviderDetailAdapter adapter = new ProviderDetailAdapter(header, info.id);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.id_recycler_view);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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
        }else if (v.getId() == R.id.next){
            BusFactory.getBus().post(new BusEvent.TabSwitcher(BusEvent.TabSwitcher.TAB_SHOPCART));
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
}
