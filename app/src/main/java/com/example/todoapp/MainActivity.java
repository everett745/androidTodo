package com.example.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


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

        checkAuth();

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
                finish();
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

    public void checkAuth() {
        SharedPreferences sh = getSharedPreferences("user", MODE_APPEND);
        String login = sh.getString("login", "");
        String psw = sh.getString("psw", "");

        LoginApi.setLogin(login);
        LoginApi.setPassword(psw);

        if (login == "" || psw == "") {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://195.133.196.6:2000/" + login)
                    .header("Authorization", "Basic " + psw)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    Intent a = new Intent(MainActivity.this, LoginActivity.class);
                    finish();
                    startActivity(a);
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override public void onResponse(Call call, Response response) throws IOException {

                    if (response.code() != 200) {
                        Intent a = new Intent(MainActivity.this, LoginActivity.class);
                        finish();
                        startActivity(a);
                    }
                }
            });
        }

    }
}

















