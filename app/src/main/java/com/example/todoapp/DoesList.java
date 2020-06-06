package com.example.todoapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

public class DoesList {

    private static ArrayList<MyDoes> list;
    private static LocalStorage ls;
    private static DataApi dataApi;

    DoesList(Context context) {
        ls = new LocalStorage(context);
        DoesList.list = ls.readDb();
        if (!LoginApi.getLogin().equals("")) DataApi.getUserTodo();
        //if (LoginApi.getLogin() != "") Log.d("TAGT", "START GETUSERTODO");
    }

    static ArrayList<MyDoes> getList() {return list;}
    static void setList(ArrayList<MyDoes> list) {
        DoesList.list = list;
        DoesList.ls.addToDos(list);
    }

    /* ДОБАВИТЬ EVENT */
    static void addTodo(MyDoes event) {
        DoesList.list.add(event);
        DoesList.ls.addToDo(event);
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
            }
        }
        DoesList.ls.readDb();
    }

    /* УДАЛИТЬ EVENT */
    public static void removeTodo(String keydoes) {
        ArrayList<MyDoes> list = new ArrayList<MyDoes>();

        for (MyDoes item: DoesList.list)
            if (item.getId().equals(keydoes)) {
                list.remove(item);
                DoesList.ls.removeTodo(item);
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

    public static boolean itemOnServer(String id, ArrayList<MyDoes> serverData) {
        for (MyDoes item: serverData)
            if (item.getId().equals(id)) return true;
        return false;
    }

    /* СИНХРОНИЗАЦИЯ С СЕРВЕРОМ */
    public static void verifyData(ArrayList<MyDoes> serverData) throws InterruptedException {

        if (DoesList.list.size() == 0) DoesList.setList(serverData);
        else {
            for (MyDoes item: DoesList.list) {
                Thread.sleep(300);
                if (!itemOnServer(item.getId(), serverData)) DataApi.addEvent(item);
            }

            for (MyDoes sItem: serverData) {
                Thread.sleep(500);
                if (!checkEventById(sItem.getId())) DataApi.removeEvent(sItem);
                else DataApi.editTodo(Objects.requireNonNull(getById(sItem.getId())));
                Thread.sleep(300);
            }
        }

    }


    // PRINT EVENT
    static void prList() {
        for (MyDoes item: DoesList.list) {
            Log.d("TAGT", "GET { " + item.getTitle() + " , " + item.getDescription() + " , " + item.getStart_time() + " , " + item.getEnd_time() + " , " + item.getId() + " , " + item.getCompleted() + " }");
        }
    }
}
