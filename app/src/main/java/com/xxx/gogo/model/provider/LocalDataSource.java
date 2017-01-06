package com.xxx.gogo.model.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xxx.gogo.model.MainDatabaseHelper;
import com.xxx.gogo.setting.SettingModel;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.HashMap;

class LocalDataSource {
    private static final String QUERY_SQL = "select id, name, phone from provider";
    private static final long REQUEST_LOAD_INTERVAL = 1000*60*60*24;

    private SQLiteOpenHelper mDb;
    private ProviderModel mCb;
    private NetDataSource mDataSource;

    LocalDataSource(ProviderModel callback){
        mCb = callback;
        mDataSource = new NetDataSource(this);
    }

    void setDbHelper(SQLiteOpenHelper dbHelper){
        mDb = dbHelper;
    }

    void addItem(final ProviderItemInfo item){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = mDb.getWritableDatabase();
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
                    db.insertOrThrow(MainDatabaseHelper.TABLE_PROVIDER, null, contentValues);

                    db.setTransactionSuccessful();

                }catch (Exception e){
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
                SQLiteDatabase db = mDb.getWritableDatabase();
                db.beginTransaction();

                try {
                    db.delete(MainDatabaseHelper.TABLE_PROVIDER, "id = ?", new String[]{info.id});
                    db.setTransactionSuccessful();

                }catch (Exception e){
                }finally {
                    db.endTransaction();
                    db.close();
                }

                mDataSource.deleteItem(info);
            }
        });
    }

    void load(){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = mDb.getReadableDatabase();
                Cursor cursor = db.rawQuery(QUERY_SQL, null);

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

                        long lastTime = SettingModel.getInstance().getLong(
                                SettingModel.KEY_LAST_LOAD_PROVIDER_TIME, 0);
                        if(System.currentTimeMillis() - lastTime > REQUEST_LOAD_INTERVAL){
                            mDataSource.load();

                            SettingModel.getInstance().putLong(SettingModel.KEY_LAST_LOAD_PROVIDER_TIME,
                                    System.currentTimeMillis());
                        }
                    }
                });
            }
        });
    }

    void onDataReady(final ArrayList<ProviderItemInfo> datas){
        if(datas != null && !datas.isEmpty()){
            ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase db = mDb.getWritableDatabase();
                    db.beginTransaction();

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
                            db.insertOrThrow(MainDatabaseHelper.TABLE_PROVIDER, null, contentValues);
                        }

                        db.setTransactionSuccessful();

                    }catch (Exception e){
                    }finally {
                        db.endTransaction();
                        db.close();
                    }
                }
            });
        }
        final HashMap<String, ProviderItemInfo> idSet = new HashMap<>();
        if(datas != null){
            for (ProviderItemInfo item : datas){
                idSet.put(item.id, item);
            }
        }
        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                mCb.onDataReady(datas, idSet);
            }
        });
    }
}
