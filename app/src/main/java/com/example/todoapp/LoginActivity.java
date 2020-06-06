package com.example.todoapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText login;
    EditText password;
    Button singIn;
    Button toReg;

    LogInTask task;

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
                if (task == null){
                    task = new LogInTask();
                    task.execute(login.getText().toString().trim(), password.getText().toString().trim());
                }
                else if(task.getStatus() == AsyncTask.Status.FINISHED) {
                    task = new LogInTask();
                    task.execute(login.getText().toString().trim(), password.getText().toString().trim());
                }
            }
        });


        toReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startRegisterActivity = new Intent(LoginActivity.this, RegistActivity.class);
                finish();
                startActivity(startRegisterActivity);
            }
        });

    }


      class  LogInTask extends AsyncTask <String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://195.133.196.6:2000/" + strings[0])
                    .header("Authorization", "Basic " + strings[1])
                    .build();

            try (Response response = client.newCall(request).execute()) {
               if (response.isSuccessful()){
                   return "";
               }
               else {
                   return "Неверный логин или пароль";
               }
            } catch (IOException e) {
                e.printStackTrace();
                return "Сервер Недоступен";
            }
        }


         @Override
        protected void onPostExecute(String error) {
            super.onPostExecute(error);
            if (error.isEmpty()){
                SharedPreferences myPref;
                myPref = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = myPref.edit();
                editor.putString("login", login.getText().toString());
                editor.putString("psw", password.getText().toString());
                editor.commit();

                Toast.makeText(getApplicationContext(),"Успешная авторизация",Toast.LENGTH_SHORT).show();
                Intent startMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(startMainActivity);
            }
            else {
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
            }
        }
    }

}
