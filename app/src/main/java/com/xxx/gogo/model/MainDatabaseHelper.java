package com.xxx.gogo.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xxx.gogo.utils.Constants;

public class MainDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "main.db";

    public static final String TABLE_SHOPCART = "shopcart";

    private static final String SQL_CREATE_SHOPCART_TABLE = "create table if not exists "
            + TABLE_SHOPCART
            + "(    index_num           INTEGER PRIMARY KEY NOT NULL, "
            + "     provider_id         INTEGER NOT NULL  , "
            + "     cat_id              INTEGER NOT NULL  , "
            + "     secondary_cat_id    INTEGER NOT NULL  , "
            + "     goods_id            INTEGER NOT NULL  , "
            + "     name                TEXT    NOT NULL, "
            + "     disc                TEXT, "
            + "     img_url             TEXT, "
            + "     price               REAL, "
            + "     count               INTEGER"
            + ");"
            + " create INDEX provider_index on " + TABLE_SHOPCART + " (provider_id); ";

    private static MainDatabaseHelper sInstance;

    public static void init(Context context){
        sInstance = new MainDatabaseHelper(context);
    }

    public static synchronized SQLiteOpenHelper getDataBaseHelper(){
        return sInstance;
    }

    private MainDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SHOPCART_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
