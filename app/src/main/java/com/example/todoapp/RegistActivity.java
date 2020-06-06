package com.example.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegistActivity extends AppCompatActivity {

    EditText login;
    EditText password;
    EditText passwordRep;
    Button singIn;
    Button toLogin;
    RegisterTask task;

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
                if (login.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Введите имя пользователя", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
                else if(passwordRep.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Повторите пароль", Toast.LENGTH_SHORT).show();
                else {
                    if (password.getText().toString().trim().equals(passwordRep.getText().toString().trim())) {
                        if (task == null) {
                            task = new RegisterTask();
                            task.execute(login.getText().toString().trim(), password.getText().toString().trim());
                        } else if (task.getStatus() == AsyncTask.Status.FINISHED) {
                            task = new RegisterTask();
                            task.execute(login.getText().toString().trim(), password.getText().toString().trim());
                        }
                    } else
                        Toast.makeText(getApplicationContext(), "Пароли должны совпадать", Toast.LENGTH_SHORT).show();
                }
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


    class RegisterTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("Authorization", "Basic " + strings[1])
                    .build();

            Request request = new Request.Builder()
                    .url("http://195.133.196.6:2000/" + strings[0])
                    .header("Authorization", "Basic " + strings[1])
                    .put(formBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()){
                    return "";
                }
                else {
                    onProgressUpdate();
                    if (response.code() == 409)
                    return "Пользователь уже существует";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Сервер Недоступен";
            }
            return null;
        }



        @Override
        protected void onPostExecute(String error) {
            super.onPostExecute(error);
            if (error.isEmpty()){
                SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(RegistActivity.this);
                SharedPreferences.Editor editor = myPreferences.edit();
                editor.putString("UserLogin", login.getText().toString());
                editor.putString("UserPsw", password.getText().toString());
                editor.commit();

                Intent a = new Intent(RegistActivity.this, MainActivity.class);
                finish();
                startActivity(a);
            }
            else {
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
            }
        }
    }


}
