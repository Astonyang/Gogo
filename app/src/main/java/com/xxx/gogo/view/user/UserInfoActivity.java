package com.xxx.gogo.view.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.model.user.UserInfoModel;

public class UserInfoActivity extends BaseToolBarActivity implements View.OnClickListener{
    private EditText mNameEditView;
    private EditText mAddrEditView;
    private EditText mTitleEditView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        createNormalToolBar(R.string.user_profile, this);

        initView();
    }

    private void initView(){
        findViewById(R.id.save).setOnClickListener(this);

        mNameEditView = (EditText) findViewById(R.id.id_name);
        mAddrEditView = (EditText) findViewById(R.id.id_addr);
        mTitleEditView = (EditText) findViewById(R.id.id_title);

        UserInfoModel.UserInfo userInfo = UserInfoModel.getInstance().getInfo();
        if(userInfo != null){
            mNameEditView.setText(userInfo.name);
            mAddrEditView.setText(userInfo.addr);
            mTitleEditView.setText(userInfo.title);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (v.getId() == R.id.save){
            UserInfoModel.UserInfo userInfo = new UserInfoModel.UserInfo();
            userInfo.name = mNameEditView.getText().toString();
            userInfo.addr = mAddrEditView.getText().toString();
            userInfo.title = mTitleEditView.getText().toString();

            UserInfoModel.getInstance().saveInfo(userInfo);

            finish();
        }
    }
}
