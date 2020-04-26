package com.example.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegistActivity extends AppCompatActivity {

    EditText login;
    EditText password;
    EditText passwordRep;
    Button singIn;
    Button toLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        passwordRep = findViewById(R.id.passwordRepeat);
        singIn = findViewById(R.id.btnLogin);
        toLogin = findViewById(R.id.btnReg);

        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText() == passwordRep.getText()) {
                    OkHttpClient client = new OkHttpClient();

                    RequestBody formBody = new FormBody.Builder()
                            .add("Authorization", "Basic" + password.getText().toString())
                            .build();

                    Request request = new Request.Builder()
                            .url("http://195.133.196.6:2000/" + login.getText().toString())
                            .put(formBody) // PUT here.
                            .header("Authorization", "Basic " + password.getText().toString())
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            Toast.makeText(RegistActivity.this, "Неверные логин или пароль", Toast.LENGTH_SHORT).show();
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                                SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(RegistActivity.this);
                                SharedPreferences.Editor editor = myPreferences.edit();
                                editor.putString("UserLogin", login.getText().toString());
                                editor.putString("UserPsw", password.getText().toString());
                                editor.commit();

                                Intent a = new Intent(RegistActivity.this, MainActivity.class);
                                finish();
                                startActivity(a);
                            }
                        }
                    });
                } else Toast.makeText(RegistActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(RegistActivity.this, LoginActivity.class);
                finish();
                startActivity(a);
            }
        });

    }

}
