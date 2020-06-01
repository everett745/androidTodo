package com.example.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.nio.channels.AsynchronousCloseException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginApi {
    private static String login;
    private static String password;

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


    public static void checkAuth() {
        String login = LoginApi.getLogin();
        String psw = LoginApi.getPassword();

        if (login == "" || psw == "") {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://195.133.196.6:2000/" + login)
                    .header("Authorization", "Basic " + psw)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        DataApi.getUserTodo();
                    }
                }
            });
        }
    }


}
