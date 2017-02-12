package com.xxx.gogo.model.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.model.UserRelatedDatabaseHelper;
import com.xxx.gogo.utils.LogUtil;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.HashMap;

class ProviderLocalDataSource {
    private static final String QUERY_SQL = "select id, name, phone from "
            + UserRelatedDatabaseHelper.TABLE_PROVIDER;

    private static final String DELETE_SQL = "delete from " + UserRelatedDatabaseHelper.TABLE_PROVIDER;

    private static final long REQUEST_LOAD_INTERVAL = 1000*60*60*24;

    private ProviderModel mCb;
    private ProviderNetDataSource mDataSource;

    ProviderLocalDataSource(ProviderModel callback){
        mCb = callback;
        mDataSource = new ProviderNetDataSource(this);
    }

    void addItem(final ProviderItemInfo item){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getWritableDatabase();
                db.beginTransaction();

                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", item.id);
                    contentValues.put("name", item.name);
                    contentValues.put("phone", item.phone);
                    contentValues.put("addr", item.addr);
                    contentValues.put("url", item.url);
                    contentValues.put("lat", item.lat);
                    contentValues.put("lng", item.lng);
                    db.insertOrThrow(UserRelatedDatabaseHelper.TABLE_PROVIDER, null, contentValues);

                    db.setTransactionSuccessful();

                }catch (Exception e){
                    LogUtil.e("add item into provider error " + e.toString());
                }finally {
                    db.endTransaction();
                    db.close();
                }

                mDataSource.addItem(item);
            }
        });
    }

    void deleteItem(final ProviderItemInfo info){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getWritableDatabase();
                db.beginTransaction();

                try {
                    db.delete(UserRelatedDatabaseHelper.TABLE_PROVIDER, "id = ?", new String[]{info.id});
                    db.setTransactionSuccessful();

                }catch (Exception e){
                    LogUtil.e("delete item from provider error " + e.toString());
                }finally {
                    db.endTransaction();
                    db.close();
                }

                mDataSource.deleteItem(info);
            }
        });
    }

    void load(){
        if(!UserManager.getInstance().isLogin()){
            return;
        }
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getReadableDatabase();
                Cursor cursor = db.rawQuery(QUERY_SQL, null);

                try {
                    final ArrayList<ProviderItemInfo> list = new ArrayList<>();
                    final HashMap<String, ProviderItemInfo> idSet = new HashMap<>();
                    if(cursor != null){
                        while (cursor.moveToNext()){
                            ProviderItemInfo info = new ProviderItemInfo();
                            info.id = cursor.getString(0);
                            info.name = cursor.getString(1);
                            info.phone = cursor.getString(2);

                            list.add(info);
                            idSet.put(info.id, info);
                        }
                        cursor.close();
                    }
                    db.close();

                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            mCb.onDataReady(list, idSet);

//                        long lastTime = SettingModel.getInstance().getLong(
//                                SettingModel.KEY_LAST_LOAD_PROVIDER_TIME, 0);
//                        if(System.currentTimeMillis() - lastTime > REQUEST_LOAD_INTERVAL){

                            mDataSource.load();

//                            SettingModel.getInstance().putLong(SettingModel.KEY_LAST_LOAD_PROVIDER_TIME,
//                                    System.currentTimeMillis());
//                        }
                        }
                    });

                }catch (Exception e){
                    LogUtil.e("load data from provider db error " + e.toString());
                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            mCb.onDataReady(null, null);
                        }
                    });
                }finally {
                    try {
                        if (cursor != null){
                            cursor.close();
                        }
                        db.close();
                    }catch (Exception e){
                        LogUtil.e("close handle of provider db error " + e.toString());
                    }
                }
            }
        });
    }

    void onDataReady(final ArrayList<ProviderItemInfo> datas){
        if(datas != null && !datas.isEmpty()){
            ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getWritableDatabase();
                    db.beginTransaction();

                    db.execSQL(DELETE_SQL);

                    try {
                        for (ProviderItemInfo item : datas){
                            ContentValues contentValues = new ContentValues();

                            contentValues.put("id", item.id);
                            contentValues.put("name", item.name);
                            contentValues.put("phone", item.phone);
                            contentValues.put("addr", item.addr);
                            contentValues.put("url", item.url);
                            contentValues.put("lat", item.lat);
                            contentValues.put("lng", item.lng);

                            db.insertOrThrow(UserRelatedDatabaseHelper.TABLE_PROVIDER, null, contentValues);
                        }

                        db.setTransactionSuccessful();

                    }catch (Exception e){
                        LogUtil.e("insert data into provider error :" + e.toString());
                    }finally {
                        db.endTransaction();
                        db.close();
                    }
                }
            });

            final HashMap<String, ProviderItemInfo> idSet = new HashMap<>();
            for (ProviderItemInfo item : datas){
                idSet.put(item.id, item);
            }

            ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                @Override
                public void run() {
                    mCb.onDataReady(datas, idSet);
                }
            });
        }
    }
}
