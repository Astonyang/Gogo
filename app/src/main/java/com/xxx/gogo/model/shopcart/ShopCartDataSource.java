package com.xxx.gogo.model.shopcart;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xxx.gogo.model.MainDatabaseHelper;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.utils.LogUtil;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.HashMap;

class ShopCartDataSource {
    private static final String QUERY_SQL = "select * from " + MainDatabaseHelper.TABLE_SHOPCART;
    private static final String CLEAR_SQL = "delete from " + MainDatabaseHelper.TABLE_SHOPCART;

    private ShopCartModel mCb;

    ShopCartDataSource(ShopCartModel model){
        mCb = model;
    }

    void add(final ArrayList<GoodsItemInfo> list){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = MainDatabaseHelper.getDataBaseHelper().getWritableDatabase();
                db.beginTransaction();

                try {
                    db.execSQL(CLEAR_SQL);

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

                        db.insertOrThrow(MainDatabaseHelper.TABLE_SHOPCART, null, contentValues);
                    }
                    db.setTransactionSuccessful();

                }catch (Exception e){
                    LogUtil.e("add to shopcart error " + e);
                }finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
    }

    void clear(){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = MainDatabaseHelper.getDataBaseHelper().getWritableDatabase();
                db.beginTransaction();

                try {
                    db.execSQL(CLEAR_SQL);
                    db.setTransactionSuccessful();

                }catch (Exception e){
                    LogUtil.e("clear shopcart error " + e);
                }finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
    }

    void load(){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = MainDatabaseHelper.getDataBaseHelper().getReadableDatabase();
                Cursor cursor = db.rawQuery(QUERY_SQL, null);

                double totalPrice = 0;
                final ArrayList<GoodsItemInfo> list = new ArrayList<>();
                final HashMap<String, GoodsItemInfo> map = new HashMap<>();
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

                        totalPrice += info.price * info.count;
                        map.put(info.generateId(), info);
                        list.add(info);
                    }
                    cursor.close();
                }
                db.close();

                final double total = totalPrice;
                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        mCb.onDataReady(list, map, total);
                    }
                });
            }
        });
    }

//    void addItem(final GoodsItemInfo item){
//        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
//            @Override
//            public void run() {
//                SQLiteDatabase db = mDb.getWritableDatabase();
//                db.beginTransaction();
//
//                try {
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("provider_id", item.providerId);
//                    contentValues.put("cat_id", item.catId);
//                    contentValues.put("secondary_cat_id", item.secondaryCatId);
//                    contentValues.put("goods_id", item.id);
//                    contentValues.put("name", item.name);
//                    contentValues.put("disc", item.introduce);
//                    contentValues.put("price", item.price);
//                    contentValues.put("img_url", item.imgUrl);
//                    contentValues.put("count", item.count);
//
//                    db.insertOrThrow(MainDatabaseHelper.TABLE_SHOPCART, null, contentValues);
//
//                    db.setTransactionSuccessful();
//
//                }catch (Exception e){
//                    LogUtil.e("add to shopcart error " + e);
//                }finally {
//                    db.endTransaction();
//                    db.close();
//                }
//            }
//        });
//    }

//    void deleteItemByIndex(final int index){
//        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
//            @Override
//            public void run() {
//                SQLiteDatabase db = mDb.getWritableDatabase();
//                db.beginTransaction();
//
//                try {
//                    db.delete(MainDatabaseHelper.TABLE_SHOPCART, "index = ?", new String[]{String.valueOf(index)});
//                    db.setTransactionSuccessful();
//
//                }catch (Exception e){
//                    LogUtil.e("delete from shopcart error " + e);
//                }finally {
//                    db.endTransaction();
//                    db.close();
//                }
//            }
//        });
//    }
}
