package com.xxx.gogo;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.otto.Subscribe;
import com.tencent.bugly.crashreport.CrashReport;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.order.OrderManager;
import com.xxx.gogo.manager.user.UserEvent;
import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.model.MainDatabaseHelper;
import com.xxx.gogo.model.UserRelatedDatabaseHelper;
import com.xxx.gogo.net.NetworkServiceFactory;
import com.xxx.gogo.setting.SettingModel;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.LogUtil;
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
    private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MainApplication application = (MainApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        StartupMetrics.Log("before MainApplication::onCreate");

        FileManager.sRootDir = getDir("user_data", MODE_PRIVATE).getAbsolutePath();

        ThreadManager.start();

        LogUtil.init(FileManager.sRootDir);

        SettingModel.getInstance().init(getApplicationContext());

        MainDatabaseHelper.init(getApplicationContext());

        NetworkServiceFactory.getInstance().create();

        UserManager.getInstance().init(FileManager.sRootDir);
        UserManager.getInstance().login();

        OrderManager.getInstance().init();

        Fresco.initialize(this);

        BusFactory.getBus().register(this);
//        测试阶段建议设置成true，发布时设置为false
        CrashReport.initCrashReport(getApplicationContext(), "2a4d8ad976", true);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);

        StartupMetrics.Log("after MainApplication::onCreate");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        ACRA.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if(event instanceof UserEvent.UserLoginSuccess){
            UserRelatedDatabaseHelper.init(getApplicationContext(),
                    UserManager.getInstance().getUserId());
        }else if (event instanceof UserEvent.UserLogout){
            UserRelatedDatabaseHelper.unInit();
        }
    }
}
