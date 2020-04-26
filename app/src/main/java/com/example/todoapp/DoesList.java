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

        /* СИНХРОНИЗАЦИЯ С СЕРВЕР, ПОКА ОТКЛЮЧЕНО */
        if (LoginApi.getLogin() != "") DataApi.getUserTodo();
    }

    static ArrayList<MyDoes> getList() {return list;}
    static void setList(ArrayList<MyDoes> list) {}

    /* ДОБАВИТЬ EVENT */
    static void addTodo(MyDoes event) {
        DoesList.list.add(event);
        DoesList.ls.addToDo(event);
        DataApi.addEvent(event);
    }
    /* ДОБАВИТЬ EVENT */
    static void addTodoLocal(MyDoes item) {
        DoesList.list.add(item);
        DoesList.ls.addToDo(item);

    }

    /* РЕДАКТИРОВАТЬ EVENT */
    static void editTodo(MyDoes newTodo) {
        for (MyDoes item: DoesList.list) {
            if (item.getId().equals(newTodo.getId())) {
                DoesList.ls.editTodo(newTodo);
                DoesList.list.set(DoesList.list.indexOf(item), newTodo);
                DataApi.editTodo(newTodo);
            }
        }
        //prList();
        DoesList.ls.readDb();
    }

    /* УДАЛИТЬ EVENT */
    public static void removeTodo(String keydoes) {
        ArrayList<MyDoes> list = new ArrayList<MyDoes>();

        for (MyDoes item: DoesList.list)
            if (item.getId().equals(keydoes)) {
                list.remove(item);
                DoesList.ls.removeTodo(item);
                DataApi.removeEvent(item);
            }
    }

    /* ИЗМЕНИТЬ ID У EVENT, НЕБХОДИМО ДЛЯ СИНХРОНИЗАЦИИ ЛОКАЛЬНОГО ID И ID СЕРВЕРА */
    static void editTodoId(MyDoes newTodo, String id) {
        for (MyDoes item: DoesList.list) {
            if (item.getId().equals(newTodo.getId())) {
                DoesList.ls.removeTodo(newTodo);
                newTodo.setId(id);
                DoesList.list.set(DoesList.list.indexOf(item), newTodo);
                DoesList.ls.addToDo(newTodo);
            }
        }
    }

    /* ПОЛУЧИТЬ EVENT ПО ID */
    static MyDoes getById(String id) {
        for (MyDoes item: DoesList.list)
            if (item.getId().equals(id)) return item;
        return null;
    }

    /* ИЗМЕНЯЕТ ЗНАЧЕНИЕ COMPLETED НА ПРОТИВОПОЛОЖНОЕ */
    public static void switchCompleted(MyDoes item) {
        item.setCompleted(!item.getCompleted());
        editTodo(item);
    }

    // WORK WITH SERVER

    /* ПРОВЕРЯЕТ ЕСТЬ ЛИ ЛОКАЛЬНЫЙ EVENT С ТАКИМ ID */
    public static boolean checkEventById(String id) {
        for (MyDoes item: DoesList.list)
            if (item.getId().equals(id)) return true;
        return false;
    }

    /* СИНХРОНИЗАЦИЯ С СЕРВЕРОМ */
    public static void verifyData(ArrayList<MyDoes> serverData)  {

        if (serverData.size() != 0) {
            for (MyDoes item: serverData)
                if (!checkEventById(item.getId())) DoesList.addTodoLocal(item);
        }

        /*if (DoesList.list.size() == 0 && serverData.size() == 0 || DoesList.list.size() == 0)
            return;
        else if (DoesList.list.size() != 0 && serverData.size() == 0) {
            for (MyDoes item: DoesList.list) {
                DataApi.addEvent(item);
            }

            return;
        } else {
            for (MyDoes item: serverData)
                if (checkEventById(item.getId()))
                    DataApi.editTodo(getById(item.getId()));
                else DataApi.removeEvent(item);

            for (MyDoes item: DoesList.list) {
                boolean check = false;
                for (MyDoes itemS: serverData) {
                    if (item.getId().equals(itemS.getId())) check = true;
                }
                if (!check) DataApi.addEvent(item);
            }
        }*/
    }


    // PRINT EVENT
    static void prList() {
        for (MyDoes item: DoesList.list) {
            Log.d("TAGT", "GET { " + item.getTitle() + " , " + item.getDescription() + " , " + item.getStart_time() + " , " + item.getEnd_time() + " , " + item.getId() + " , " + item.getCompleted() + " }");
        }
    }
}
