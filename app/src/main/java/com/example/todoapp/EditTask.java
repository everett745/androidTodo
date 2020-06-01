package com.example.todoapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class EditTask extends AppCompatActivity {

    TextView titlepage, addtitle, adddesc, adddate;
    EditText title, description, start_time, end_time;
    CheckBox addNotification;
    Button btnSaveTask, btnCancel;
    String keydoes;
    String unixDateStart, unixDateEnd;
    Boolean addTimeNotification = false;

    Calendar dateAndTime=Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        titlepage = findViewById(R.id.titlepage);
        titlepage.setText("Изменить задачу");

        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        //adddate = findViewById(R.id.adddate);

        addNotification = findViewById(R.id.addNotification);

        title = findViewById(R.id.titledoes);
        description = findViewById(R.id.descdoes);
        //start_time = findViewById(R.id.dateStart);
        end_time = findViewById(R.id.dateEnd);

        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnSaveTask.setText("Сохранить изменения");
        btnCancel = findViewById(R.id.btnCancel);

        /*start_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) setDate();
            }
        });*/
        end_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) setDate1();
            }
        });

        Intent intent = getIntent();
        //unixDateStart = intent.getStringExtra("startTime");
        unixDateEnd = intent.getStringExtra("endTime");

        try {
            Date start = new Date(Integer.parseInt(unixDateStart) * 1000L);
            Date end = new Date(Integer.parseInt(unixDateEnd) * 1000L);

            String pattern = "dd.MM.yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            //start_time.setText(simpleDateFormat.format(start));
            end_time.setText(simpleDateFormat.format(end));
        } catch (Throwable e) {
        }


        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));
        description.setText(intent.getStringExtra("description"));
        keydoes = intent.getStringExtra("ID");

        // начальные значения у выбора времени
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        addNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(EditTask.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            addTimeNotification = true;

                            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            dateAndTime.set(Calendar.MINUTE, minute);
                            dateAndTime.set(Calendar.SECOND, 0);
                            dateAndTime.set(Calendar.MILLISECOND, 0);
                        }
                    }, hour, minute, android.text.format.DateFormat.is24HourFormat(EditTask.this));

                    timePickerDialog.show();
                } else addTimeNotification = false;

            }
        });

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateEnd = unixDateEnd != null ? unixDateEnd : " ";
                String desc = description.getText().toString().length() == 0 ? " " : description.getText().toString();
                DoesList.editTodo(new MyDoes(keydoes, " ", dateEnd, title.getText().toString(), desc));


                if (addTimeNotification) {
                    Intent intent = new Intent(EditTask.this, notification.class);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("desc", description.getText().toString());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(EditTask.this, Integer.parseInt(keydoes), intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    if (dateAndTime.getTimeInMillis() - calendar.getTimeInMillis() >= 86400000)
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);
                    else
                        alarmManager.set(AlarmManager.RTC_WAKEUP, dateAndTime.getTimeInMillis(), pendingIntent);

                    if (!addNotification.isChecked()) pendingIntent.cancel();
                }

                EditTask.super.onBackPressed();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTask.super.onBackPressed();
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

        //adddate.setTypeface(MLight);
        //start_time.setTypeface(MMedium);
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

               /* start_time.setText(DateUtils.formatDateTime(EditTask.this,
                        dateAndTime.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));*/
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
