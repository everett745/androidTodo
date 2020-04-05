package com.example.todoapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class DoesList {

    private static ArrayList<MyDoes> list;
    private static LocalStorage ls;

    DoesList(Context context) {
        ls = new LocalStorage(context);
        DoesList.list = ls.readDb();
    }

    static ArrayList<MyDoes> getList() {return list;}
    static void setList(ArrayList<MyDoes> list) {
        DoesList.list = list;
        DoesList.saveLocalStorage();
    }

    static void editTodo(MyDoes newTodo) {
        //Log.d("TEST", "GET { " + newTodo.getTitle() + " , " + newTodo.getDescription() + " , " + newTodo.getStart_time() + " , " + newTodo.getEnd_time() + "}");

        for (MyDoes item: DoesList.list) {
            if (item.getId().equals(newTodo.getId())) DoesList.list.set(DoesList.list.indexOf(item), newTodo);
        }
        DoesList.saveLocalStorage();
    }

    public static void removeTodo(String keydoes) {
        ArrayList<MyDoes> list = new ArrayList<MyDoes>();

        for (MyDoes item: DoesList.list)
            if (!item.getId().equals(keydoes)) list.add(item);

        DoesList.setList(list);
    }


    private static void saveLocalStorage() {
        DoesList.ls.clearDB();
        DoesList.ls.addToDos(list);
    }


    static void prList() {
        for (MyDoes item: DoesList.list) {
            Log.d("TEST", "GET { " + item.getTitle() + " , " + item.getDescription() + " , " + item.getStart_time() + " , " + item.getEnd_time() + " , " + item.getId() + " }");
        }
    }

}
