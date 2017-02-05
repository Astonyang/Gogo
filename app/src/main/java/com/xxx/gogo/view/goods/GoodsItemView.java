package com.xxx.gogo.view.goods;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class GoodsItemView extends LinearLayout implements View.OnClickListener {
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
        Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
        getContext().startActivity(intent);
    }
}
