package com.xxx.gogo.setting;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingModel {
    private static final String PREF_FILE_NAME = "xxx_gogo_pref";

    public static final String KEY_LAST_LOAD_PROVIDER_TIME = "last_load_provider_time";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_LAST_PURGE_DATA_TIME= "last_purge_time";
    public static final String KEY_LAST_LOAD_CAT_FROM_SERVER_TIME = "last_load_cat_from_server";

    private Context mContext;
    private SharedPreferences mPref;

    @SuppressWarnings("all")
    private static class InstanceHolder{
        private static SettingModel sInstance = new SettingModel();
    }

    private SettingModel(){
    }

    public static SettingModel getInstance(){
        return InstanceHolder.sInstance;
    }

    public void init(Context context){
        mContext = context;
        mPref = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value){
        mPref.edit().putString(key, value).apply();
    }

    public void putInt(String key, int value){
        mPref.edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value){
        mPref.edit().putLong(key, value).apply();
    }

    public void putBoolean(String key, boolean value){
        mPref.edit().putBoolean(key, value).apply();
    }

    public String getString(String key, String defaultValue){
        return mPref.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue){
        return mPref.getInt(key, defaultValue);
    }
    public long getLong(String key, long defaultValue){
        return mPref.getLong(key, defaultValue);
    }
    public boolean getBoolean(String key, boolean defaultValue){
        return mPref.getBoolean(key, defaultValue);
    }
}
