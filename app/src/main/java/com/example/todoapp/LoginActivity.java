package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOError;
import java.io.IOException;
import java.util.ResourceBundle;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    EditText login;
    EditText password;
    Button singIn;
    Button toReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        singIn = findViewById(R.id.btnLogin);
        toReg = findViewById(R.id.btnReg);

        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://195.133.196.6:2000/" + login.getText().toString())
                        .header("Authorization", "Basic " + password.getText().toString())
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        Log.d("TAGT", "ERROR " + e.toString());
                        //Toast.makeText(LoginActivity.this, "Неверные логин или пароль", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            SharedPreferences myPref;
                            myPref = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = myPref.edit();
                            editor.putString("login", login.getText().toString());
                            editor.putString("psw", password.getText().toString());
                            editor.commit();

                            //Log.d("TAGT", "response: " + response);
                            //Toast.makeText(LoginActivity.this, "Успешная авторизация", Toast.LENGTH_SHORT).show();
                            Intent a = new Intent(LoginActivity.this, MainActivity.class);
                            finish();
                            startActivity(a);
                        } catch (Throwable e) {
                            //Log.d("TAGT", "calll " + response.request());
                            //Log.d("TAGT", "ERROR " + e.toString());
                        }
                    }
                });
            }
        });

        toReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(LoginActivity.this, RegistActivity.class);
                finish();
                startActivity(a);
            }
        });

    }

}