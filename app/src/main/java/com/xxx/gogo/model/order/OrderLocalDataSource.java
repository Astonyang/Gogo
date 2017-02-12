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
import com.xxx.gogo.utils.ThreadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class OrderLocalDataSource {
    private static final String ORDER_DETAIL_PATH = "order_detail";

    private static final String QUERY_SQL = "select * from "
            + UserRelatedDatabaseHelper.TABLE_ORDER;

    private static final String DELETE_SQL = "delete from " + UserRelatedDatabaseHelper.TABLE_ORDER
            + " where id = ";

    private Callback mCb;

    OrderLocalDataSource(Callback callback){
        mCb = callback;
    }

    void add(final ArrayList<OrderItemInfo> orderList,
                    final ArrayList<ArrayList<OrderItemDetailInfo>> detailList){
        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (OrderItemInfo orderItemInfo : orderList){
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
            LogUtil.e("add item into order_info error " + e.toString());
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

    void delete(String orderId){

    }

    void modifyState(String orderId){

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

                final List<OrderItemInfo> orderItemInfoList = new ArrayList<>();
                final List<List<OrderItemDetailInfo>> detailInfoList = new ArrayList<>();

                try {
                    if(cursor != null){
                        while (cursor.moveToNext()){
                            OrderItemInfo info = new OrderItemInfo();
                            info.id = cursor.getString(0);
                            info.storeName = cursor.getString(1);
                            info.price = cursor.getDouble(2);
                            info.goodsNum = cursor.getInt(3);
                            info.startTime = cursor.getLong(4);
                            info.state = cursor.getInt(5);

                            orderItemInfoList.add(info);

                            if(mCb != null){
                                mCb.onLoaded(orderItemInfoList);
                            }

//                            List<OrderItemDetailInfo> detailInfos = loadDetail(info.id);
//                            if(detailInfos != null){
//                                detailInfoList.add(detailInfos);
//                            }
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

    interface Callback{
        void onLoaded(List<OrderItemInfo> orderItemInfoList);
//                      List<List<OrderItemDetailInfo>> detailList);
        void onCanceled(String orderId);
    }
}
