package com.example.todoapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;


public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyDoes> myDoes;


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
        final String getTitle = myDoes.get(i).getTitle();
        final String getDescription = myDoes.get(i).getDescription();
        //final String getStart_time = myDoes.get(i).getStart_time();
        final String getEnd_time = myDoes.get(i).getEnd_time();
        final String getID = myDoes.get(i).getId();
        final Boolean getCompleted = myDoes.get(i).getCompleted();

        try {
            //Date start = new Date(Integer.parseInt(myDoes.get(i).getStart_time()) * 1000L);
            Date rawEndTime = new Date(Long.parseLong(getEnd_time) * 1000L);


            String pattern = " dd.MM.yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            //Start_time = simpleDateFormat.format(start);
            String formatedEndTime = simpleDateFormat.format(rawEndTime);

            myViewHolder.endTime.setText(formatedEndTime);
        } catch (Throwable e) {}

        myViewHolder.title.setText(myDoes.get(i).getTitle());
        myViewHolder.description.setText(myDoes.get(i).getDescription());


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa = new Intent(context, EditTask.class);
                aa.putExtra("title", getTitle);
                aa.putExtra("description", getDescription);
                //aa.putExtra("startTime", getStart_time);
                aa.putExtra("endTime", getEnd_time);
                aa.putExtra("ID", getID);

                context.startActivity(aa);
            }
        });


        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent aa = new Intent(context, MainActivity.class);
                if (myDoes.get(i).getCompleted()) {
                    Intent intent = new Intent(context, notification.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(getID), intent, 0);
                    pendingIntent.cancel();

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
                } else {
                    myViewHolder.itemView.setBackground(myViewHolder.itemView.getContext().getDrawable(R.drawable.bgitemdoes));
                }
                DoesList.switchCompleted(myDoes.get(i));
            }
        });

        if (getCompleted) {
            myViewHolder.itemView.setBackground(myViewHolder.itemView.getContext().getDrawable(R.drawable.bgitemdoesfinish));
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
            //startTime = (TextView) itemView.findViewById(R.id.dateStart);
            endTime = (TextView) itemView.findViewById(R.id.dateEnd);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

}
