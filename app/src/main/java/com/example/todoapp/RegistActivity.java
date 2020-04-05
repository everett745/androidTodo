package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
                if (password.getText().equals(passwordRep.getText())) {
                    if (LoginApi.signIn(login.getText().toString(), password.getText().toString())) {
                        Toast.makeText(RegistActivity.this, "Вы зарегистрировались", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(RegistActivity.this, MainActivity.class);
                        startActivity(a);
                    }
                    else Toast.makeText(RegistActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();

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
