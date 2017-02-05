package com.xxx.gogo.view.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.user.UserEvent;
import com.xxx.gogo.view.user.ChangePasswordActivity;

public class SettingActivity extends BaseToolBarActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        createNormalToolBar(R.string.setting, this);

        findViewById(R.id.id_change_pwd).setOnClickListener(this);
        findViewById(R.id.id_change_pwd).setOnClickListener(this);
        findViewById(R.id.id_quit_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (R.id.id_change_pwd == v.getId()){
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if (R.id.id_quit_btn == v.getId()){
            BusFactory.getBus().post(new UserEvent.UserLogout());
            finish();
        }
    }
}
