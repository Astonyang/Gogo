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

public class UserRegisterActivity extends BaseToolBarActivity implements View.OnClickListener{
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        createNormalToolBar(R.string.register, this);

        initView();

        BusFactory.getBus().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusFactory.getBus().unregister(this);
    }

    private void initView(){
        mLoadingDialog = DialogHelper.createDialog(this);
        findViewById(R.id.go_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }else if(v.getId() == R.id.go_register){
            String userName = ((EditText)findViewById(R.id.id_edit_phone)).getText().toString();
            String pwd = ((EditText)findViewById(R.id.pass_edit)).getText().toString();
            String confirmPwd = ((EditText)findViewById(R.id.confirm_pass_edit)).getText().toString();
            String checksum = ((EditText)findViewById(R.id.check_num_edit)).getText().toString();
            String invitationNum = ((EditText)findViewById(R.id.invite_edit)).getText().toString();

            if(!pwd.equals(confirmPwd) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)
                    || TextUtils.isEmpty(checksum) || TextUtils.isEmpty(invitationNum)){
                ToastManager.showToast(this, getString(R.string.input_again));
                return;
            }
            UserManager.getInstance().register(userName, pwd, checksum, invitationNum);
            mLoadingDialog.show();
        }
    }

    @Subscribe
    public void onEvent(Object event){
        if(event instanceof UserEvent.UserRegisterSuccess){
            finish();
            mLoadingDialog.dismiss();
        }else if (event instanceof UserEvent.UserRegisterFail){
            mLoadingDialog.dismiss();
            ToastManager.showToast(this, getString(R.string.input_again));
        }
    }
}
