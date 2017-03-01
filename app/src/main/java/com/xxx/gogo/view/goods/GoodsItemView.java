package com.xxx.gogo.view.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.xxx.gogo.model.goods.GoodsDetailInfoModel;
import com.xxx.gogo.model.goods.GoodsItemInfo;

public class GoodsItemView extends LinearLayout implements View.OnClickListener {

    private GoodsItemInfo mInfo;

    public GoodsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GoodsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoodsItemView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        GoodsDetailInfoModel.getInstance().setInfo(mInfo);

        Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
        getContext().startActivity(intent);
    }

    public void setGoodsInfo(GoodsItemInfo info){
        mInfo = info;
    }
}
