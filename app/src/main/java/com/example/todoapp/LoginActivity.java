package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
                if (LoginApi.signIn(login.getText().toString(), password.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Успешная авторизация", Toast.LENGTH_SHORT).show();
                    Intent a = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(a);
                } else Toast.makeText(LoginActivity.this, "Неверные логин или пароль", Toast.LENGTH_SHORT).show();
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
