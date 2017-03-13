package com.xxx.gogo.utils;

import android.util.Log;

import com.xxx.gogo.BuildConfig;
import com.xxx.gogo.net.NetworkServiceFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogUtil {
    private static final String TAG = "xx_gogo";
    private static final String LOG_FILE_NAME = "xx_gogo.log";
    private static final String DISPOSITION_NAME = "log";

    public static boolean DEBUG = BuildConfig.DEBUG;
    public static boolean ENABLE_LOG2FILE = false;

    private static FileOutputStream sFile;

    private static String sLogDir;

    public static void init(String logDir){
        sLogDir = logDir;
    }

    public static void unInit(){
        if(ENABLE_LOG2FILE){
            ThreadManager.postTask(ThreadManager.TYPE_LOG, new Runnable() {
                @Override
                public void run() {
                    if(sFile != null){
                        try {
                            sFile.close();
                            sFile.flush();
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
        }
        if(ENABLE_LOG2FILE){
            final String content = formatMessage(format, args);

            ThreadManager.postTask(ThreadManager.TYPE_LOG, new Runnable() {
                @Override
                public void run() {
                    doLog2File(content);
                }
            });
        }
    }

    public static void d(String format, Object... args) {
        if(DEBUG){
            final String content = formatMessage(format, args);
            Log.d(TAG, content);
        }
        if(ENABLE_LOG2FILE){
            final String content = formatMessage(format, args);

            ThreadManager.postTask(ThreadManager.TYPE_LOG, new Runnable() {
                @Override
                public void run() {
                    doLog2File(content);
                }
            });
        }
    }

    public static void e(String format, Object... args) {
        final String content = formatMessage(format, args);
        Log.e(TAG, content);
        if(ENABLE_LOG2FILE){
            ThreadManager.postTask(ThreadManager.TYPE_LOG, new Runnable() {
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
                    sFile = new FileOutputStream(new File(sLogDir, LOG_FILE_NAME), true);
                }
                str += System.getProperty("line.separator");
                sFile.write(str.getBytes());
            } catch (IOException e) {
            }
    }

    public static void uploadLog(){
        File file = new File(sLogDir, LOG_FILE_NAME);
        if(!file.exists()){
            return;
        }
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(DISPOSITION_NAME, file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is log";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        Call<ResponseBody> call = NetworkServiceFactory.getInstance()
                .getService().uploadFile(description, body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
