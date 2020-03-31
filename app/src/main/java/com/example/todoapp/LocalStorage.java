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
        contentValues.put(DBHelper.KEY_ID, Integer.parseInt(todo.getKeydoes()));
        contentValues.put(DBHelper.KEY_TITLE, todo.getTitledoes());
        contentValues.put(DBHelper.KEY_DESC, todo.getDescdoes());
        contentValues.put(DBHelper.KEY_DATE, todo.getDatedoes());
        //contentValues.put(DBHelper.KEY_COMPLETED, todo.getCompleted().toString());

        this.database.insert(DBHelper.TABLE, null, contentValues);
    }

    void editTodo(String id, MyDoes newTodo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_ID, id);
        contentValues.put(DBHelper.KEY_TITLE, newTodo.getTitledoes());
        contentValues.put(DBHelper.KEY_DESC, newTodo.getDescdoes());
        contentValues.put(DBHelper.KEY_DATE, newTodo.getDatedoes());
        //contentValues.put(DBHelper.KEY_COMPLETED, newTodo.getCompleted());

        this.database.update(DBHelper.TABLE, contentValues, "_id="+id, null);
    }

    ArrayList<MyDoes> readDb() {
        final ArrayList<MyDoes> list = new ArrayList<MyDoes>();
        Cursor cursor = this.database.query(DBHelper.TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int descIndex = cursor.getColumnIndex(DBHelper.KEY_DESC);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);

            do {

                MyDoes p = new MyDoes(cursor.getString(titleIndex), cursor.getString(dateIndex), cursor.getString(descIndex), cursor.getString(idIndex));
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
