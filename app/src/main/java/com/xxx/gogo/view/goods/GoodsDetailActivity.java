package com.xxx.gogo.view.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.shopcart.ShopCartEvent;
import com.xxx.gogo.model.goods.GoodsDetailInfoModel;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.utils.CommonUtils;
import com.xxx.gogo.utils.DialogHelper;

public class GoodsDetailActivity extends BaseToolBarActivity
        implements View.OnClickListener{

    private GoodsItemInfo mInfo;
    private TextView mTvCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_item_detail);

        createNormalToolBar(R.string.goods_item_detail, this);

        initView();

        BusFactory.getBus().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BusFactory.getBus().unregister(this);

        GoodsDetailInfoModel.getInstance().setInfo(null);
    }

    private void initView(){
        //// TODO: 17/3/1 fix it later
        mInfo = GoodsDetailInfoModel.getInstance().getInfo();
        if(mInfo == null){
            finish();
            return;
        }
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(
                R.id.id_img_goods_detail_info);
        simpleDraweeView.setImageURI(mInfo.largeImgUrl);

        TextView tvName = (TextView) findViewById(R.id.id_name);
        tvName.setText(mInfo.name);

        mTvCount = (TextView) findViewById(R.id.id_count_value);
        mTvCount.setText(String.valueOf(mInfo.count));

        TextView tvUnitPrice = (TextView) findViewById(R.id.id_unit_price);
        tvUnitPrice.setText(CommonUtils.formatPrice(mInfo.price));

        TextView tvSpecific = (TextView) findViewById(R.id.id_specific_value);
        tvSpecific.setText(mInfo.spec);

        findViewById(R.id.id_count_value).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (v.getId() == R.id.id_count_value){
            DialogHelper.showCountSelectDialog(this, mInfo);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if(event instanceof ShopCartEvent.ShopCartDataChanged){
            mTvCount.setText(String.valueOf(mInfo.count));
        }
    }
}
