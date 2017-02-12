package com.xxx.gogo.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.xxx.gogo.BuildConfig;

import java.util.HashMap;

public class ThreadManager {
    public static final int TYPE_UI = 0;
    public static final int TYPE_DB = 1;
    public static final int TYPE_FILE = 2;
    public static final int TYPE_WORKER = 3;

    private static HandlerThread sDbThread;
    private static HandlerThread sFileThread;
    private static HandlerThread sWorkerThread;

    private static HashMap<Integer, Handler> sMaps = new HashMap<>();

    public synchronized static void start(){
        sMaps.put(TYPE_UI, new Handler(Looper.getMainLooper()));

        sDbThread = new HandlerThread("xxx_db_thread");
        sDbThread.start();
        sMaps.put(TYPE_DB, new Handler(sDbThread.getLooper()));

        sFileThread = new HandlerThread("xxx_file_thread");
        sFileThread.start();
        sMaps.put(TYPE_FILE, new Handler(sFileThread.getLooper()));

        sWorkerThread = new HandlerThread("xxx_worker_thread");
        sWorkerThread.start();
        sMaps.put(TYPE_WORKER, new Handler(sWorkerThread.getLooper()));
    }

    public synchronized static void stop(){
        sDbThread.quit();
        sFileThread.quit();
        sWorkerThread.quit();
    }

    public synchronized static void postTask(int type, Runnable runnable){
        Handler handler = sMaps.get(type);
        if(handler != null){
            if(handler.getLooper() == Looper.myLooper()){
                runnable.run();
            }else {
                handler.post(runnable);
            }
        }
    }

    public synchronized static void postTask(int type, Runnable runnable, long delayed){
        Handler handler = sMaps.get(type);
        if(handler != null){
            handler.postDelayed(runnable, delayed);
        }
    }

    public static void currentlyOn(int type){
        if(BuildConfig.DEBUG){
            if(Looper.myLooper() != sMaps.get(type).getLooper()){
                throw new RuntimeException("run on invalid thread");
            }
        }
    }
}
