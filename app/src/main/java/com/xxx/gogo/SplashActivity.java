package com.xxx.gogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.squareup.otto.Subscribe;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.user.UserEvent;
import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.utils.LogUtil;
import com.xxx.gogo.utils.ThreadManager;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BusFactory.getBus().register(this);

        if(UserManager.getInstance().shouldStartMainActivity()){
            ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            }, 1000);
        }else {
            LogUtil.v("shouldStartMainActivity return false");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BusFactory.getBus().unregister(this);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if(event instanceof UserEvent.UserLoginSuccess
                || event instanceof UserEvent.UserLoginFail){
            startMainActivity();
        }
    }

    private void startMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0, 0);

        finish();
    }
}
