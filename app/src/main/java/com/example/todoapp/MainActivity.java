package com.example.todoapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    TextView titlepage, subtitlepage;
    View btnAddNew;

    RecyclerView ourdoes;
    DoesAdapter doesAdapter;
    DoesList dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();


        if (checkAuth()) {
            titlepage = findViewById(R.id.titlepage);
            subtitlepage = findViewById(R.id.subtitlepage);

            btnAddNew = findViewById(R.id.btnAddNew);

            // import font
            Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
            Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

            // customize font
            titlepage.setTypeface(MMedium);
            subtitlepage.setTypeface(MLight);

            //btnAddNew.setTypeface(MLight);

            btnAddNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(MainActivity.this, NewTaskAct.class);
                    startActivity(a);
                }
            });


            // working with data
            ourdoes = findViewById(R.id.ourdoes);
            ourdoes.setLayoutManager(new LinearLayoutManager(this));

            // get toDos
            dl = new DoesList(MainActivity.this);

            doesAdapter = new DoesAdapter(MainActivity.this, DoesList.getList());
            ourdoes.setAdapter(doesAdapter);
            doesAdapter.notifyDataSetChanged();
        }

    }

    public boolean checkAuth() {
        SharedPreferences sh = getSharedPreferences("user", MODE_APPEND);
        String login = sh.getString("login", "");
        String psw = sh.getString("psw", "");
        LoginApi.setLogin(login);
        LoginApi.setPassword(psw);

        if (login == "") {
            Toast.makeText(getApplicationContext(), "Для первого запуска необходима авторизация" , Toast.LENGTH_LONG).show();

            Intent a = new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(a);
        } else {
            LoginApi.setLogin(login);
            LoginApi.setPassword(psw);
            return true;
        }
        return false;
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Напоминание о задачах";
            String description = "Уведомления о предстоящих задачах";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        dl = new DoesList(MainActivity.this);
        doesAdapter = new DoesAdapter(MainActivity.this, DoesList.getList());
        ourdoes.setAdapter(doesAdapter);
        doesAdapter.notifyDataSetChanged();
    }

}

