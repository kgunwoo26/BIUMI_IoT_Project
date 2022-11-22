package com.example.biumi_iot_project;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
    final static String TAG = "SQLiteDBTest";

    public MyDBHelper(Context context) {
        super(context, UserContract.DB_NAME, null, UserContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, getClass().getName() + ".onCreate()");
        db.execSQL(UserContract.Users.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG, getClass().getName() + ".onUpgrade()");
        db.execSQL(UserContract.Users.DELETE_TABLE);
        onCreate(db);
    }

    public void insert(String alarm, String history_h, String history_m, String building, String floor, String h_case) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.Users.KEY_ALARM, alarm);
        values.put(UserContract.Users.KEY_HISTORY_H, history_h);
        values.put(UserContract.Users.KEY_HISTORY_M, history_m);
        values.put(UserContract.Users.KEY_BUILDING, building);
        values.put(UserContract.Users.KEY_FLOOR, floor);
        values.put(UserContract.Users.KEY_CASE, h_case);

        db.insert(UserContract.Users.TABLE_NAME, null, values);
    }

    public Cursor getAllUsersByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(UserContract.Users.TABLE_NAME, null, null, null, null, null, null);
    }

    public void delete(String alarm, String building, String floor) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM " + UserContract.Users.TABLE_NAME +
                " WHERE " + UserContract.Users.KEY_ALARM + " = '" + alarm +
                "' AND " + UserContract.Users.KEY_BUILDING + " = '" + building +
                "' AND " + UserContract.Users.KEY_FLOOR + " = '" + floor + "';");
    }
}

final class UserContract {
    public static final String DB_NAME = "user.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserContract() {
    }

    /* Inner class that defines the table contents */
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String KEY_ALARM = "alarm";
        public static final String KEY_HISTORY_H = "history_h";
        public static final String KEY_HISTORY_M = "history_m";
        public static final String KEY_BUILDING = "building";
        public static final String KEY_FLOOR = "floor";
        public static final String KEY_CASE = "h_case";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_ALARM + TEXT_TYPE + COMMA_SEP +
                KEY_HISTORY_H + TEXT_TYPE + COMMA_SEP +
                KEY_HISTORY_M + TEXT_TYPE + COMMA_SEP +
                KEY_BUILDING + TEXT_TYPE + COMMA_SEP +
                KEY_FLOOR + TEXT_TYPE + COMMA_SEP +
                KEY_CASE + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}