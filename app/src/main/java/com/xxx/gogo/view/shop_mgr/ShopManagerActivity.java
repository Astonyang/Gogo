package com.xxx.gogo.view.shop_mgr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.model.shop_mgr.ShopInfoModel;

public class ShopManagerActivity extends BaseToolBarActivity implements View.OnClickListener{
    private EditText mNameEditView;
    private EditText mAddrEditView;
    private EditText mOwnerEditView;
    private EditText mPhoneEditView;
    private EditText mStartTimeEditView;
    private EditText mEndTimeEditView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_manager);

        createNormalToolBar(R.string.restaurant_manager, this);

        initView();
    }

    private void initView(){
        findViewById(R.id.save).setOnClickListener(this);

        mNameEditView = (EditText) findViewById(R.id.id_name);
        mAddrEditView = (EditText) findViewById(R.id.id_addr);
        mPhoneEditView = (EditText) findViewById(R.id.id_phone);
        mOwnerEditView = (EditText) findViewById(R.id.id_owner);
        mStartTimeEditView = (EditText) findViewById(R.id.id_send_time_begin);
        mEndTimeEditView = (EditText) findViewById(R.id.id_sent_time_end);

        ShopInfoModel.ShopInfo info = ShopInfoModel.getInstance().getInfo();
        if(info != null){
            mNameEditView.setText(info.name);
            mAddrEditView.setText(info.addr);
            mOwnerEditView.setText(info.owner);
            mPhoneEditView.setText(info.phone);
            mStartTimeEditView.setText(String.valueOf(info.startTime));
            mEndTimeEditView.setText(String.valueOf(info.endTime));
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (v.getId() == R.id.save){
            ShopInfoModel.ShopInfo info = new ShopInfoModel.ShopInfo();
            info.name = mNameEditView.getText().toString();
            info.addr = mAddrEditView.getText().toString();
            info.owner = mOwnerEditView.getText().toString();
            info.phone = mPhoneEditView.getText().toString();
            info.startTime = Short.valueOf(mStartTimeEditView.getText().toString());
            info.endTime = Short.valueOf(mEndTimeEditView.getText().toString());

            ShopInfoModel.getInstance().save(info);

            finish();
        }
    }
}
