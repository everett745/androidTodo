package com.example.todoapp;

import android.content.SharedPreferences;

public class LoginApi {
    private static String login;

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        LoginApi.login = login;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        LoginApi.password = password;
    }

    private static String password;


}
