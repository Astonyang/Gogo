package com.xxx.gogo.model.often_buy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.model.UserRelatedDatabaseHelper;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.net.NetworkProtocolFactory;
import com.xxx.gogo.net.NetworkResponse;
import com.xxx.gogo.net.NetworkServiceFactory;
import com.xxx.gogo.utils.LogUtil;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class OftenBuyLocalDataSource {
    private static final String QUERY_GOODS_SQL = "select * from "
            + UserRelatedDatabaseHelper.TABLE_OFTEN_BUY;

    private static final String CLEAR_SQL = "delete from "
            + UserRelatedDatabaseHelper.TABLE_OFTEN_BUY + " where provider_id = ";

    private OftenBuyNetDataSource mDataSource;
    private Callback mCb;

    OftenBuyLocalDataSource(Callback cb){
        mCb = cb;
        mDataSource = new OftenBuyNetDataSource();
    }

    void load(){
        if(!UserManager.getInstance().isLogin()){
            return;
        }
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                boolean ret = true;
                final ArrayList<GoodsItemInfo> list = new ArrayList<>();

                try {
                    SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getReadableDatabase();
                    Cursor cursor = db.rawQuery(QUERY_GOODS_SQL, null);

                    if(cursor != null){
                        while (cursor.moveToNext()){
                            GoodsItemInfo info = new GoodsItemInfo();

                            info.providerId = cursor.getLong(1);
                            info.catId = cursor.getLong(2);
                            info.secondaryCatId = cursor.getLong(3);
                            info.id = cursor.getLong(4);
                            info.name = cursor.getString(5);
                            info.introduce = cursor.getString(6);
                            info.imgUrl = cursor.getString(7);
                            info.price = cursor.getDouble(8);
                            info.count = cursor.getInt(9);
                            info.buyCount = cursor.getInt(10);
                            info.buyTime = cursor.getLong(11);

                            list.add(info);
                        }
                        cursor.close();
                    }
                    db.close();
                }catch (Exception | Error e){
                    ret = false;
                    LogUtil.e("load oftenbuy data failed exception : " + e.toString());
                }
                final boolean success = ret;

                if(mCb != null){
                    if(success){
                        if(list.isEmpty()){
                            mDataSource.load(OftenBuyLocalDataSource.this);
                        }else {
                            mCb.onLoadSuccess(list);
                        }
                    }else {
                        mCb.onLoadFail();
                    }
                }
            }
        });
    }

    void add(final ArrayList<GoodsItemInfo> list){
        mDataSource.commit(list);

        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getWritableDatabase();
                db.beginTransaction();

                try {
                    for (GoodsItemInfo item : list){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("provider_id", item.providerId);
                        contentValues.put("cat_id", item.catId);
                        contentValues.put("secondary_cat_id", item.secondaryCatId);
                        contentValues.put("goods_id", item.id);
                        contentValues.put("name", item.name);
                        contentValues.put("disc", item.introduce);
                        contentValues.put("price", item.price);
                        contentValues.put("img_url", item.imgUrl);
                        contentValues.put("count", item.count);
                        contentValues.put("buy_count", item.buyCount);
                        contentValues.put("buy_timestamp", item.buyTime);

                        db.insertOrThrow(UserRelatedDatabaseHelper.TABLE_OFTEN_BUY, null, contentValues);
                    }
                    db.setTransactionSuccessful();

                }catch (Exception e){
                    LogUtil.e("add data to oftenbuy failed exception: " + e);
                }finally {
                    db.endTransaction();
                    db.close();
                }

                if(mCb != null){
                    mCb.onAdded();
                }
            }
        });
    }

    void removeByProviderId(final String providerId){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getWritableDatabase();
                db.beginTransaction();

                try {
                    db.execSQL(CLEAR_SQL + providerId);
                    db.setTransactionSuccessful();

                }catch (Exception e){
                    LogUtil.e("clear oftenbuy error " + e);
                }finally {
                    db.endTransaction();
                    db.close();
                }

                if(mCb != null){
                    mCb.onRemoved(providerId);
                }
            }
        });
    }

    void onDataReady(final List<GoodsItemInfo> goodsItemInfoList){
        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                if(goodsItemInfoList != null){
                    mCb.onLoadSuccess(goodsItemInfoList);
                }else {
                    mCb.onLoadFail();
                }
            }
        });
    }

    interface Callback{
        void onAdded();
        void onRemoved(String providerId);
        void onLoadSuccess(List<GoodsItemInfo> goods);
        void onLoadFail();
    }
}
