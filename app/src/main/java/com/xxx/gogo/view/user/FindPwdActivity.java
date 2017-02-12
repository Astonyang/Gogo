package com.xxx.gogo.view.user;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.squareup.otto.Subscribe;
import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.user.UserEvent;
import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.utils.DialogHelper;
import com.xxx.gogo.utils.ToastManager;

public class FindPwdActivity extends BaseToolBarActivity implements View.OnClickListener{
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        findViewById(R.id.send_phone).setOnClickListener(this);
        createNormalToolBar(R.string.forget_password, this);
        mLoadingDialog = DialogHelper.createLoadingDialog(this);

        BusFactory.getBus().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusFactory.getBus().unregister(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if (v.getId() == R.id.send_phone){
            EditText editText = (EditText) findViewById(R.id.id_edit_phone);
            String phone = editText.getText().toString();
            if(TextUtils.isEmpty(phone)){
                ToastManager.showToast(this, getString(R.string.input_again));
                return;
            }
            UserManager.getInstance().findPassword(phone);
            mLoadingDialog.show();
        }
    }

    @Subscribe
    public void onEvent(Object event){
        if(event instanceof UserEvent.UserFindPasswordSuccess){
            mLoadingDialog.dismiss();
            finish();
        }else if(event instanceof UserEvent.UserFindPasswordFail){
            mLoadingDialog.dismiss();
            ToastManager.showToast(this, getString(R.string.find_pwd_again));
        }
    }
}
