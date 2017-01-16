package com.xxx.gogo.view.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;

public class UserInfoActivity extends BaseToolBarActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        createNormalToolBar(R.string.user_profile, this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }
    }
}
