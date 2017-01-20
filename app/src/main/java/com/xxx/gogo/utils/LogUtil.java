package com.xxx.gogo.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class LogUtil {
    public static boolean DEBUG = true;
    public static boolean ENABLE_LOG2FILE = false;
    private static String TAG = "xxx_gogo";

    private static FileOutputStream sFile;

    public static void unInit(){
        if(ENABLE_LOG2FILE){
            ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
                @Override
                public void run() {
                    if(sFile != null){
                        try {
                            sFile.close();
                            sFile = null;
                        }catch (Exception e){
                        }
                    }
                }
            });
        }
    }

    public static void v(String format, Object... args) {
        if(DEBUG){
            final String content = formatMessage(format, args);
            Log.v(TAG, content);
            if(ENABLE_LOG2FILE){
                ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
                    @Override
                    public void run() {
                        doLog2File(content);
                    }
                });
            }
        }
    }

    public static void d(String format, Object... args) {
        if(DEBUG){
            final String content = formatMessage(format, args);
            Log.d(TAG, content);
            if(ENABLE_LOG2FILE){
                ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
                    @Override
                    public void run() {
                        doLog2File(content);
                    }
                });
            }
        }
    }

    public static void e(String format, Object... args) {
        final String content = formatMessage(format, args);
        Log.e(TAG, content);
        if(ENABLE_LOG2FILE){
            ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
                @Override
                public void run() {
                    doLog2File(content);
                }
            });
        }
    }

    private static String formatMessage(String format, Object... args) {
        String msg = (args == null || args.length < 1) ?
                format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "null";
        for (StackTraceElement stack : trace) {
            String clazzName = stack.getClassName();
            if (!clazzName.equals(LogUtil.class.getName())) {
                clazzName = clazzName.substring(clazzName.lastIndexOf('.') + 1);
                caller = clazzName + "." + stack.getMethodName() + " (" + stack.getLineNumber() + ")";
                break;
            }
        }

        return String.format(Locale.US, "[%d] %s: %s",
                Thread.currentThread().getId(), caller, msg);
    }

    private static void doLog2File(String str){
        if(ENABLE_LOG2FILE)
            try {
                if(sFile == null){
                    sFile = new FileOutputStream(new File("sdcard/xxx_gogo", "logxxx"), true);
                }
                str += System.getProperty("line.separator");
                sFile.write(str.getBytes());
            } catch (IOException e) {
            }
    }
}
