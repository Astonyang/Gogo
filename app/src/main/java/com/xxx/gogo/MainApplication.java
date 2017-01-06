package com.xxx.gogo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.xxx.gogo.setting.SettingModel;

public class MainApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        SettingModel.getInstance().init(getApplicationContext());
        Fresco.initialize(this);
    }
}
