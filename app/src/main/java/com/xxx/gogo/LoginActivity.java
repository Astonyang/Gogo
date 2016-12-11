package com.xxx.gogo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.xxx.gogo.utils.ToastManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mPhoneEditView;
    private EditText mPasswordEdit;
    private ImageView mCancelPhone;
    private ImageView mCancelPassword;

    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_view);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(myToolbar);

        initView();
        initLoadingDialog();
    }

    private void initView(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowCustomEnabled(true);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View view = LayoutInflater.from(this).inflate(R.layout.toolbar_login, null);
            actionBar.setCustomView(view, layoutParams);

            View imgView = view.findViewById(R.id.login_bar_back);
            imgView.setOnClickListener(this);
        }
        findViewById(R.id.forget_pass).setOnClickListener(this);
        findViewById(R.id.fast_register).setOnClickListener(this);
        findViewById(R.id.go_login).setOnClickListener(this);

        mCancelPhone = (ImageView)findViewById(R.id.cancel_phone);
        mCancelPassword = (ImageView)findViewById(R.id.cancel_pass);
        mPhoneEditView = (EditText)findViewById(R.id.id_edit_phone);
        mPasswordEdit = (EditText)findViewById(R.id.id_edit_password);
        mPhoneEditView.addTextChangedListener(new MyTextWatcher(mCancelPhone));
        mPasswordEdit.addTextChangedListener(new MyTextWatcher(mCancelPassword));
    }

    private void initLoadingDialog(){
        mLoadingDialog = new Dialog(this, R.style.CustomDialog);
        mLoadingDialog.setContentView(R.layout.dialog_login_loading);
        mLoadingDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_bar_back){
            finish();
        }else if(v.getId() == R.id.go_login){
            doLogin();
        }else if(v.getId() == R.id.fast_register){
            Intent intent = new Intent(this, UserRegisterActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.forget_pass){
            Intent intent = new Intent(this, ForgetPwdActivity.class);
            startActivity(intent);
        }
    }

    private void doLogin(){
        String user = mPhoneEditView.getText().toString();
        String pass = mPasswordEdit.getText().toString();
        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)){
            ToastManager.showToast(this, getResources().getString(R.string.input_correct_pass_phone));
        }else {
            mLoadingDialog.show();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.dismiss();
                    finish();
                }
            }, 1000);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private ImageView mView;

        MyTextWatcher(ImageView view){
            mView = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Log.v("123---", "beforeTextChanged: " + s.toString() + " count= " + count);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Log.v("123---", "onTextChanged: " + s.toString() + " count= " + count);
            if(TextUtils.isEmpty(s)){
                mView.setVisibility(View.GONE);
            }else {
                mView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //Log.v("123---", "afterTextChanged: " + s.toString());
        }
    }
}
