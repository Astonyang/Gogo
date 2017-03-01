package com.xxx.gogo.view.store_mgr;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.model.store_mgr.StoreInfoModel;
import com.xxx.gogo.utils.ToastManager;

public class StoreManagerActivity extends BaseToolBarActivity implements View.OnClickListener{
    private EditText mNameEditView;
    private EditText mAddrEditView;
    private EditText mOwnerEditView;
    private EditText mPhoneEditView;
    private TextView mStartTimeView;
    private TextView mEndTimeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manager);

        createNormalToolBar(R.string.restaurant_manager, this);

        initView();
    }

    private void initView(){
        findViewById(R.id.save).setOnClickListener(this);

        mNameEditView = (EditText) findViewById(R.id.id_name);
        mAddrEditView = (EditText) findViewById(R.id.id_addr);
        mPhoneEditView = (EditText) findViewById(R.id.id_phone);
        mOwnerEditView = (EditText) findViewById(R.id.id_owner);
        mStartTimeView = (TextView) findViewById(R.id.id_send_time_begin);
        mEndTimeView = (TextView) findViewById(R.id.id_sent_time_end);

        mStartTimeView.setOnClickListener(this);
        mEndTimeView.setOnClickListener(this);

        StoreInfoModel.getInstance().getInfo(new StoreInfoModel.Callback() {
            @Override
            public void onLoaded(StoreInfoModel.StoreInfo info) {
                if(info != null){
                    mNameEditView.setText(info.name);
                    mAddrEditView.setText(info.addr);
                    mOwnerEditView.setText(info.owner);
                    mPhoneEditView.setText(info.phone);
                    mStartTimeView.setText(info.startTime);
                    mEndTimeView.setText(info.endTime);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (v.getId() == R.id.save){
            StoreInfoModel.StoreInfo info = new StoreInfoModel.StoreInfo();
            info.name = mNameEditView.getText().toString();
            info.addr = mAddrEditView.getText().toString();
            info.owner = mOwnerEditView.getText().toString();
            info.phone = mPhoneEditView.getText().toString();
            info.startTime = mStartTimeView.getText().toString();
            info.endTime = mEndTimeView.getText().toString();

            if(checkIfDataValid(info)){
                StoreInfoModel.getInstance().save(info);
                finish();
            }else {
                ToastManager.showToast(this, getString(R.string.input_again));
            }
        }else if (v.getId() == R.id.id_send_time_begin){
            showTimePicker(mStartTimeView);
        }else if (v.getId() == R.id.id_sent_time_end){
            showTimePicker(mEndTimeView);
        }
    }

    private void showTimePicker(final TextView target){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, android.R.style.Theme_DeviceDefault_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        target.setText("" + hourOfDay + " : " + minute);
                    }
                }, 0, 0, false);

        timePickerDialog.show();
    }

    private boolean checkIfDataValid(StoreInfoModel.StoreInfo info){
        return !TextUtils.isEmpty(info.addr) && !TextUtils.isEmpty(info.name)
                && !TextUtils.isEmpty(info.endTime) && !TextUtils.isEmpty(info.owner)
                && !TextUtils.isEmpty(info.phone) && !TextUtils.isEmpty(info.startTime);
    }
}
