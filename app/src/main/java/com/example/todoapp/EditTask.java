package com.example.todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditTask extends AppCompatActivity {

    TextView titlepage, addtitle, adddesc, adddate;
    EditText title, description, start_time, end_time;
    Button btnSaveTask, btnCancel;
    String keydoes;

    Calendar dateAndTime=Calendar.getInstance();

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

        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));
        start_time.setText(intent.getStringExtra("startTime"));
        end_time.setText(intent.getStringExtra("endTime"));
        description.setText(intent.getStringExtra("description"));
        keydoes = intent.getStringExtra("ID");

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set in DoesList
                DoesList.editTodo(new MyDoes(keydoes, start_time.getText().toString(), end_time.getText().toString(), title.getText().toString(), description.getText().toString()));

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
