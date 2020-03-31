package com.example.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final  String DATABASE_NAME = "does";
    public static final  String TABLE = "do";

    public static final  String KEY_ID = "_id";
    public static final  String KEY_TITLE = "title";
    public static final  String KEY_DESC = "descr";
    public static final  String KEY_DATE = "date";
    public static final  String KEY_COMPLETED = "false";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + "("
                + KEY_ID + " text primary key,"
                + KEY_TITLE + " text,"
                + KEY_DESC + " text,"
                + KEY_DATE + " text" + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE);

        onCreate(db);
    }
}
