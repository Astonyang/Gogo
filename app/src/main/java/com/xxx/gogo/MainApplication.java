package com.xxx.gogo;

import android.app.Application;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.xxx.gogo.net.VolleyWrapper;
import com.xxx.gogo.setting.SettingModel;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.StartupMetrics;
import com.xxx.gogo.utils.ThreadManager;

public class MainApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        StartupMetrics.Log("before MainApplication::onCreate");

        FileManager.sRootDir = getFilesDir().getAbsolutePath();

        SettingModel.getInstance().init(getApplicationContext());
        Fresco.initialize(this);
        ThreadManager.start();
        VolleyWrapper.getInstance().init(this);

        StartupMetrics.Log("after MainApplication::onCreate");
    }
}
