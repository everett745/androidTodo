package com.example.todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EditTask extends AppCompatActivity {

    TextView titlepage, addtitle, adddesc, adddate;
    EditText title, description, start_time, end_time;
    Button btnSaveTask, btnCancel;
    String keydoes;
    String unixDateStart, unixDateEnd;

    Calendar dateAndTime=Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        titlepage = findViewById(R.id.titlepage);
        titlepage.setText("Изменить дело");

        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        adddate = findViewById(R.id.adddate);

        title = findViewById(R.id.titledoes);
        description = findViewById(R.id.descdoes);
        start_time = findViewById(R.id.dateStart);
        end_time = findViewById(R.id.dateEnd);

        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnSaveTask.setText("Сохранить изменения");
        btnCancel = findViewById(R.id.btnCancel);

        start_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) setDate();
            }
        });
        end_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) setDate1();
            }
        });

        Intent intent = getIntent();
        unixDateStart = intent.getStringExtra("startTime");
        unixDateEnd = intent.getStringExtra("endTime");

        try {
            Date start = new Date(Integer.parseInt(unixDateStart) * 1000L);
            Date end = new Date(Integer.parseInt(unixDateEnd) * 1000L);

            String pattern = "dd.MM.yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            start_time.setText(simpleDateFormat.format(start));
            end_time.setText(simpleDateFormat.format(end));
        } catch (Throwable e) {
        }


        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));
        description.setText(intent.getStringExtra("description"));
        keydoes = intent.getStringExtra("ID");

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoesList.editTodo(new MyDoes(keydoes, unixDateStart, unixDateEnd, title.getText().toString(), description.getText().toString()));

                Intent a = new Intent(EditTask.this, MainActivity.class);
                finish();
                startActivity(a);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(EditTask.this, MainActivity.class);
                finish();
                startActivity(a);
            }
        });


        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        titlepage.setTypeface(MMedium);

        addtitle.setTypeface(MLight);
        title.setTypeface(MMedium);

        adddesc.setTypeface(MLight);
        description.setTypeface(MMedium);

        adddate.setTypeface(MLight);
        start_time.setTypeface(MMedium);
        end_time.setTypeface(MMedium);

        btnSaveTask.setTypeface(MMedium);
        btnCancel.setTypeface(MLight);

    }

    // вызов окна смены даты START
    public void setDate() {
        // установка обработчика выбора даты
        DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                unixDateStart = Long.toString(dateAndTime.getTimeInMillis() / 1000L);

                start_time.setText(DateUtils.formatDateTime(EditTask.this,
                        dateAndTime.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
            }
        };
        new DatePickerDialog(EditTask.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    // вызов окна смены даты END
    public void setDate1() {
        // установка обработчика выбора даты
        DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                unixDateEnd = Long.toString(dateAndTime.getTimeInMillis() / 1000L);

                end_time.setText(DateUtils.formatDateTime(EditTask.this,
                        dateAndTime.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
            }
        };
        new DatePickerDialog(EditTask.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}
