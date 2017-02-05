package com.xxx.gogo;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.xxx.gogo.net.VolleyWrapper;
import com.xxx.gogo.setting.SettingModel;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.StartupMetrics;
import com.xxx.gogo.utils.ThreadManager;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;


@ReportsCrashes(
        formUri = "http://",
        formUriBasicAuthLogin = "name",
        formUriBasicAuthPassword = "password",
        mode = ReportingInteractionMode.DIALOG,
        reportType = HttpSender.Type.JSON
)
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        ACRA.init(this);
    }
}
