package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyDoes> myDoes;

    /*private float downX;
    private float downY;
    private static int MIN_DISTANCE;*/

    public DoesAdapter(Context c, ArrayList<MyDoes> p) {
        context = c;
        myDoes = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does, viewGroup, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        String Start_time = "не задано", End_time = "не задано";
        try {
            Date start = new Date(Integer.parseInt(myDoes.get(i).getStart_time()) * 1000L);
            Date end = new Date(Integer.parseInt(myDoes.get(i).getEnd_time()) * 1000L);

            String pattern = " dd.MM.yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Start_time = simpleDateFormat.format(start);
            End_time = simpleDateFormat.format(end);

            myViewHolder.startTime.setText(Start_time);
            myViewHolder.endTime.setText(End_time);
        } catch (Throwable e) {
            Log.d("TAGT", "onBindViewHolder: " + Start_time + "  -   " + End_time);
        }

        myViewHolder.title.setText(myDoes.get(i).getTitle());
        myViewHolder.description.setText(myDoes.get(i).getDescription());


        final String getTitle = myDoes.get(i).getTitle();
        final String getDescription = myDoes.get(i).getDescription();
        final String getStart_time = myDoes.get(i).getStart_time();
        final String getEnd_time = myDoes.get(i).getEnd_time();
        final String getID = myDoes.get(i).getId();
        final Boolean getCompleted = myDoes.get(i).getCompleted();



        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa = new Intent(context, EditTask.class);
                aa.putExtra("title", getTitle);
                aa.putExtra("description", getDescription);
                aa.putExtra("startTime", getStart_time);
                aa.putExtra("endTime", getEnd_time);
                aa.putExtra("ID", getID);

                context.startActivity(aa);
            }
        });

        /* УДАЛЕНИЕ ПО СВАЙПУ */
        /*
        myViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        downX = event.getX();
                        downY = event.getY();

                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        float upX = event.getX();
                        float deltaX = downX - upX;

                        if (Math.abs(deltaX) > MIN_DISTANCE/2) {
                            if (deltaX > 0) {
                                Toast.makeText(context, "Запись удалена", Toast.LENGTH_SHORT).show();
                                Intent aa = new Intent(context, MainActivity.class);
                                if (myDoes.get(i).getCompleted())
                                    DoesList.removeTodo(getKeyDoes);

                                context.startActivity(aa);
                                return true;
                            }
                        }
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        float upX = event.getX();
                        float deltaX = downX - upX;

                        if (Math.abs(deltaX) > MIN_DISTANCE/2) {
                            if (deltaX > 0) {
                                Toast.makeText(context, "Запись удалена", Toast.LENGTH_SHORT).show();
                                Intent aa = new Intent(context, MainActivity.class);
                                if (myDoes.get(i).getCompleted())
                                    DoesList.removeTodo(getKeyDoes);

                                context.startActivity(aa);
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        }); */

        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent aa = new Intent(context, MainActivity.class);
                if (myDoes.get(i).getCompleted()) {
                    DoesList.removeTodo(getID);
                    context.startActivity(aa);
                }
                return true;
            }
        });

        CheckBox checkBox = (CheckBox)  myViewHolder.itemView.findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    myViewHolder.itemView.setBackground(myViewHolder.itemView.getContext().getDrawable(R.drawable.bgitemdoesfinish));
                    DoesList.switchCompleted(myDoes.get(i));
                } else {
                    myViewHolder.itemView.setBackground(myViewHolder.itemView.getContext().getDrawable(R.drawable.bgitemdoes));
                    DoesList.switchCompleted(myDoes.get(i));
                }
             }
        });

        if (getCompleted) {
            checkBox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, startTime, endTime;
        CheckBox checkBox;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titledoes);
            description = (TextView) itemView.findViewById(R.id.descdoes);
            startTime = (TextView) itemView.findViewById(R.id.dateStart);
            endTime = (TextView) itemView.findViewById(R.id.dateEnd);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

}
