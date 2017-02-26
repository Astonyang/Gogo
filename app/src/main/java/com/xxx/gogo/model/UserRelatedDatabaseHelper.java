package com.xxx.gogo.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xxx.gogo.utils.Constants;

public class UserRelatedDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME_SUFFIX = "_main.db";

    public static final String TABLE_PROVIDER = "provider";
    public static final String TABLE_OFTEN_BUY = "often_buy";
    public static final String TABLE_ORDER= "order_info";
    //public static final String TABLE_ORDER_COMPLETED = "completed_order_info";
    //public static final String TABLE_ORDER_PENDING = "pending_order_info";

    private static final String SQL_CREATE_PROVIDER_TABLE = "create table if not exists "
            + TABLE_PROVIDER
            + "(    id      INTEGER PRIMARY KEY NOT NULL, "
            + "     name    TEXT    NOT NULL, "
            + "     phone   TEXT, "
            + "     addr    TEXT, "
            + "     url     TEXT, "
            + "     lat     REAL, "
            + "     lng     REAL "
            + ")";

    private static final String SQL_CREATE_OFTEN_BUY_TABLE = "create table if not exists "
            + TABLE_OFTEN_BUY
            + "(    index_num           INTEGER PRIMARY KEY NOT NULL, "
            + "     provider_id         INTEGER NOT NULL  , "
            + "     cat_id              INTEGER NOT NULL  , "
            + "     secondary_cat_id    INTEGER NOT NULL  , "
            + "     goods_id            INTEGER NOT NULL  , "
            + "     name                TEXT    NOT NULL, "
            + "     disc                TEXT, "
            + "     img_url             TEXT, "
            + "     price               REAL, "
            + "     count               INTEGER,"
            + "     buy_count           INTEGER,"
            + "     buy_timestamp       LONG "
            + ");"
            + " create INDEX provider_index on " + TABLE_OFTEN_BUY + " (provider_id); ";

    private static final String SQL_CREATE_COMPLETED_ORDER_TABLE = "create table if not exists "
            + TABLE_ORDER
            + "(    id      TEXT PRIMARY KEY NOT NULL, "
            + "     store_name    TEXT    , "
            + "     total_price   REAL, "
            + "     goods_num    INT, "
            + "     time     LONG, "
            + "     state   INT"
            + ")";

//    private static final String SQL_CREATE_COMPLETED_ORDER_TABLE = "create table if not exists "
//            + TABLE_ORDER_COMPLETED
//            + "(    id      TEXT PRIMARY KEY NOT NULL, "
//            + "     store_name    TEXT    , "
//            + "     total_price   REAL, "
//            + "     goods_num    INT, "
//            + "     time     LONG, "
//            + "     state   INT"
//            + ")";
//
//    private static final String SQL_CREATE_PENDING_ORDER_TABLE = "create table if not exists "
//            + TABLE_ORDER_PENDING
//            + "(    id      TEXT PRIMARY KEY NOT NULL, "
//            + "     store_name    TEXT    , "
//            + "     total_price   REAL, "
//            + "     goods_num    INT, "
//            + "     time     LONG, "
//            + "     state   INT"
//            + ")";

    private static UserRelatedDatabaseHelper sInstance;

    public static void init(Context context, String userId){
        sInstance = new UserRelatedDatabaseHelper(context, userId + DATABASE_NAME_SUFFIX);
    }

    public static void unInit(){
        sInstance = null;
    }

    public static synchronized SQLiteOpenHelper getDataBaseHelper(){
        return sInstance;
    }

    private UserRelatedDatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, Constants.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PROVIDER_TABLE);
        db.execSQL(SQL_CREATE_OFTEN_BUY_TABLE);
        db.execSQL(SQL_CREATE_COMPLETED_ORDER_TABLE);
        db.execSQL(SQL_CREATE_COMPLETED_ORDER_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
