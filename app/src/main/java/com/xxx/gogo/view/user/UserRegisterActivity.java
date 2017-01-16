package com.xxx.gogo.view.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;

public class UserRegisterActivity extends BaseToolBarActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        createNormalToolBar(R.string.register, this);

        initView();
    }

    private void initView(){
//        findViewById(R.id.forget_pass).setOnClickListener(this);
//        findViewById(R.id.fast_register).setOnClickListener(this);
//        findViewById(R.id.go_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }
    }
}
