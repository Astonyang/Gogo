package com.xxx.gogo.model.order;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.model.UserRelatedDatabaseHelper;
import com.xxx.gogo.utils.CryptoUtil;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.LogUtil;
import com.xxx.gogo.utils.Preconditions;
import com.xxx.gogo.utils.ThreadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class OrderLocalDataSource {
    //private static final long PURGE_DATA_INTERVAL = 1000*60*60*24*3;
    private static final int MAX_ORDER_CACHE_COUNT = 30;

    public static final String ORDER_DETAIL_PATH = "order_detail";

    private static final String QUERY_ORDER_SQL = "select * from "
            + UserRelatedDatabaseHelper.TABLE_ORDER + " order by time DESC";

    private static final String DELETE_ORDER_SQL = "delete from "
            + UserRelatedDatabaseHelper.TABLE_ORDER
            + " where id = ";

    private static final String MODIFY_ORDER_SQL = "update "
            + UserRelatedDatabaseHelper.TABLE_ORDER
            + " set state = ? where id = ?";

    private static final String PURGE_ORDER_SQL = "delete "
            + UserRelatedDatabaseHelper.TABLE_ORDER
            + " where time < ";

    private Callback mCb;

    OrderLocalDataSource(Callback callback){
        mCb = callback;
    }

    void add(final List<OrderItemInfo> orderList,
                    final List<List<OrderItemDetailInfo>> detailList){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (OrderItemInfo orderItemInfo : orderList){
                    Preconditions.checkArgument(orderItemInfo.state == OrderItemInfo.STATE_CREATED);

                    addOrderItemInfo(orderItemInfo);

                    addOrderDetailInfo(orderItemInfo.id, detailList.get(i++));
                }
            }
        });
    }

    private void addOrderItemInfo(OrderItemInfo item){
        SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", item.id);
            contentValues.put("store_name", item.storeName);
            contentValues.put("total_price", item.price);
            contentValues.put("goods_num", item.goodsNum);
            contentValues.put("time", item.startTime);
            contentValues.put("state", item.state);

            db.insertOrThrow(UserRelatedDatabaseHelper.TABLE_ORDER, null, contentValues);

            db.setTransactionSuccessful();

        }catch (Exception e){
            LogUtil.e("add item into pending order_info error " + e.toString());
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    private void addOrderDetailInfo(String orderId, List<OrderItemDetailInfo> infoList){
        Gson gson = new Gson();
        String strJson = gson.toJson(infoList);
        byte[] data = CryptoUtil.encrypt(strJson.getBytes());
        FileManager.writeFile(ORDER_DETAIL_PATH + File.separator + orderId, data);
    }

    void append(OrderItemInfo orderItemInfo, List<OrderItemDetailInfo> detailInfos){

    }

    void delete(final String orderId){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getWritableDatabase();
                db.beginTransaction();

                try {
                    db.execSQL(DELETE_ORDER_SQL + orderId);
                    db.setTransactionSuccessful();

                }catch (Exception e){
                    LogUtil.e("delete item from order error " + e.toString());
                }finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
    }

    void modifyState(final String orderId, final int state){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getWritableDatabase();
                db.beginTransaction();

                try {
                    db.execSQL(MODIFY_ORDER_SQL, new Object[]{state, orderId});
                    db.setTransactionSuccessful();

                }catch (Exception e){
                    LogUtil.e("modify item from order error " + e.toString());
                }finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
    }

    public void load(){
        if(!UserManager.getInstance().isLogin()){
            return;
        }
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getReadableDatabase();
                Cursor cursor = db.rawQuery(QUERY_ORDER_SQL, null);

                final List<OrderItemInfo> orderItemInfoList = new ArrayList<>();
                try {
                    if(cursor != null){
                        while (cursor.moveToNext()){
                            if(orderItemInfoList.size() < MAX_ORDER_CACHE_COUNT){
                                OrderItemInfo info = new OrderItemInfo();
                                info.id = cursor.getString(0);
                                info.storeName = cursor.getString(1);
                                info.price = cursor.getDouble(2);
                                info.goodsNum = cursor.getInt(3);
                                info.startTime = cursor.getLong(4);
                                info.state = cursor.getInt(5);

                                orderItemInfoList.add(info);
                            }else {
                                purge(cursor.getLong(4));
                            }
                        }
                        if(mCb != null){
                            mCb.onLoaded(orderItemInfoList);
                        }
                    }

                }catch (Exception e){
                    LogUtil.e("load data from order_info db error " + e.toString());
                }finally {
                    try {
                        if (cursor != null){
                            cursor.close();
                        }
                        db.close();
                    }catch (Exception e){
                        LogUtil.e("close handle of order_info db error " + e.toString());
                    }
                }
            }
        });
    }

    private List<OrderItemDetailInfo> loadDetail(String orderId){
        byte[] data = FileManager.readFile(ORDER_DETAIL_PATH + File.separator + orderId);
        Gson gson = new Gson();
        byte[] rawData = CryptoUtil.deEncrypt(data);
        if(rawData != null && rawData.length > 0){
            return gson.fromJson(new String(rawData),
                    new TypeToken<List<OrderItemDetailInfo>>(){}.getType());
        }
        return null;
    }

    private void purge(long time){
        SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getWritableDatabase();
        try {
            db.execSQL(PURGE_ORDER_SQL + time);

        }catch (Exception e){
            LogUtil.e("purge data from order_info db error " + e.toString());
        }finally {
            try {
                db.close();
            }catch (Exception e){
                LogUtil.e("close handle of order_info db error " + e.toString());
            }
        }

    }

//    private void checkIfNeedPurge(){
//        long lastPurgeTime = SettingModel.getInstance().getLong(
//                SettingModel.KEY_LAST_PURGE_DATA_TIME, 0);
//        if(System.currentTimeMillis() - lastPurgeTime < PURGE_DATA_INTERVAL){
//            return;
//        }
//
//        SQLiteDatabase db = UserRelatedDatabaseHelper.getDataBaseHelper().getWritableDatabase();
//        Cursor cursor = db.rawQuery(QUERY_ORDER_SQL, null);
//        if(cursor.getCount() < MAX_ORDER_CACHE_COUNT){
//            return;
//        }
//        try {
//            db.execSQL();
//
//            SettingModel.getInstance().putLong(SettingModel.KEY_LAST_PURGE_DATA_TIME,
//                    System.currentTimeMillis());
//
//        }catch (Exception e){
//            LogUtil.e("purge data from order_info db error " + e.toString());
//        }finally {
//            try {
//                cursor.close();
//                db.close();
//            }catch (Exception e){
//                LogUtil.e("close handle of order_info db error " + e.toString());
//            }
//        }
//    }

    interface Callback{
        void onLoaded(List<OrderItemInfo> orderItemInfoList);
    }
}
