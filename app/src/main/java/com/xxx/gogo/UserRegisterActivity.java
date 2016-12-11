package com.xxx.gogo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserRegisterActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(myToolbar);

        initView();
    }

    private void initView(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowCustomEnabled(true);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View view = LayoutInflater.from(this).inflate(R.layout.toolbar_user_register, null);
            actionBar.setCustomView(view, layoutParams);

            View imgView = view.findViewById(R.id.user_register_bar_back);
            imgView.setOnClickListener(this);
        }
//        findViewById(R.id.forget_pass).setOnClickListener(this);
//        findViewById(R.id.fast_register).setOnClickListener(this);
//        findViewById(R.id.go_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.user_register_bar_back){
            finish();
        }
    }
}
