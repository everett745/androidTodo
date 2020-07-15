package com.example.todoapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Random;


public class NewTaskAct extends AppCompatActivity {

    TextView titlepage, addtitle, adddesc, adddate;
    EditText title, description, start_time, end_time;
    Button btnSaveTask, btnCancel;
    CheckBox addNotification;
    Long unixDateStart;
    String unixDateEnd = null;
    Boolean addTimeNotification = false;
    String doesNum = Integer.toString(new Random().nextInt(15000));

    Calendar dateAndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        titlepage = findViewById(R.id.titlepage);

        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        //adddate = findViewById(R.id.adddate);

        addNotification = findViewById(R.id.addNotification);

        title = findViewById(R.id.titledoes);
        description = findViewById(R.id.descdoes);
        //start_time = findViewById(R.id.dateStart);
        end_time = findViewById(R.id.dateEnd);

        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);

       /* start_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        Calendar calendar = Calendar.getInstance();

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        addNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(NewTaskAct.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            addTimeNotification = true;

                            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            dateAndTime.set(Calendar.MINUTE, minute);
                            dateAndTime.set(Calendar.SECOND, 0);
                            dateAndTime.set(Calendar.MILLISECOND, 0);
                        }
                    }, hour, minute, android.text.format.DateFormat.is24HourFormat(NewTaskAct.this));

                    timePickerDialog.show();
                } else addTimeNotification = false;
            }
        });

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateEnd = unixDateEnd != null ? unixDateEnd : " ";
                String desc = description.getText().toString().length() == 0 ? " " : description.getText().toString();
                MyDoes event = new MyDoes(doesNum, " ", dateEnd, title.getText().toString(), desc);
                DoesList.addTodo(event);

                if (addTimeNotification) {
                    Intent intent = new Intent(NewTaskAct.this, Notification.class);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("desc", description.getText().toString());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(NewTaskAct.this, Integer.parseInt(doesNum), intent, 0);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                    // если больше дня, то повторять оповещение каждый день
                    if (dateAndTime.getTimeInMillis() - calendar.getTimeInMillis() >= 86400000)
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);
                    else alarmManager.set(AlarmManager.RTC_WAKEUP, dateAndTime.getTimeInMillis(), pendingIntent);
                }

                NewTaskAct.super.onBackPressed();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTaskAct.super.onBackPressed();
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
        final long[] unixTimestamp = {0};
        // установка обработчика выбора даты
        DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                unixDateStart = dateAndTime.getTimeInMillis() / 1000L;

                /*start_time.setText(DateUtils.formatDateTime(NewTaskAct.this,
                        dateAndTime.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));*/
            }
        };
        new DatePickerDialog(NewTaskAct.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    // вызов окна смены даты END
    public void setDate1() {
        final long[] unixTimestamp = {0};
        // установка обработчика выбора даты
        DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                unixDateEnd = Long.toString(dateAndTime.getTimeInMillis() / 1000L);

                end_time.setText(DateUtils.formatDateTime(NewTaskAct.this,
                        dateAndTime.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
            }
        };
        new DatePickerDialog(NewTaskAct.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}
