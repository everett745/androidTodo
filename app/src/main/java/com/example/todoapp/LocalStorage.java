package com.example.todoapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

class LocalStorage {

    private DBHelper dbHelper;
    private SQLiteDatabase database;


    LocalStorage(Context context) {
        this.dbHelper = new DBHelper(context);
        this.database = dbHelper.getWritableDatabase();
    }

    void addToDos(ArrayList<MyDoes> list) {
        for (MyDoes todo: list) {
            addToDo(todo);
        }
    }

    void addToDo(MyDoes todo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_ID, Integer.parseInt(todo.getId()));
        contentValues.put(DBHelper.KEY_TITLE, todo.getTitle());
        contentValues.put(DBHelper.KEY_DESC, todo.getDescription());
        contentValues.put(DBHelper.KEY_START, todo.getStart_time());
        contentValues.put(DBHelper.KEY_END, todo.getEnd_time());

        this.database.insert(DBHelper.TABLE, null, contentValues);
    }

    void editTodo(String id, MyDoes newTodo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_ID, id);
        contentValues.put(DBHelper.KEY_TITLE, newTodo.getTitle());
        contentValues.put(DBHelper.KEY_DESC, newTodo.getDescription());
        contentValues.put(DBHelper.KEY_START, newTodo.getStart_time());
        contentValues.put(DBHelper.KEY_END, newTodo.getEnd_time());

        this.database.update(DBHelper.TABLE, contentValues, "_id="+id, null);
    }

    ArrayList<MyDoes> readDb() {
        final ArrayList<MyDoes> list = new ArrayList<MyDoes>();
        Cursor cursor = this.database.query(DBHelper.TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int descIndex = cursor.getColumnIndex(DBHelper.KEY_DESC);
            int startIndex = cursor.getColumnIndex(DBHelper.KEY_START);
            int endIndex = cursor.getColumnIndex(DBHelper.KEY_END);

            do {

                MyDoes p = new MyDoes(cursor.getString(idIndex), cursor.getString(startIndex), cursor.getString(endIndex),cursor.getString(titleIndex), cursor.getString(descIndex));
                list.add(p);
            } while (cursor.moveToNext());

        }

        cursor.close();
        return list;
    }

    void clearDB() {
        this.database.delete(DBHelper.TABLE, null, null);
    }
}
