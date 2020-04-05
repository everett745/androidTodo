package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

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

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.titledoes.setText(myDoes.get(i).getTitledoes());
        myViewHolder.descdoes.setText(myDoes.get(i).getDescdoes());
        myViewHolder.datedoes.setText(myDoes.get(i).getDatedoes());

        final String getTitleDoes = myDoes.get(i).getTitledoes();
        final String getDescDoes = myDoes.get(i).getDescdoes();
        final String getDateDoes = myDoes.get(i).getDatedoes();
        final String getKeyDoes = myDoes.get(i).getKeydoes();



        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa = new Intent(context, EditTask.class);
                aa.putExtra("titledoes", getTitleDoes);
                aa.putExtra("descdoes", getDescDoes);
                aa.putExtra("datedoes", getDateDoes);
                aa.putExtra("keydoes", getKeyDoes);

                context.startActivity(aa);
            }
        });

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
                if (myDoes.get(i).getCompleted())
                    DoesList.removeTodo(getKeyDoes);

                context.startActivity(aa);
                return true;
            }
        });

        CheckBox checkBox = (CheckBox)  myViewHolder.itemView.findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    myViewHolder.itemView.setBackground(myViewHolder.itemView.getContext().getDrawable(R.drawable.bgitemdoesfinish));
                    myDoes.get(i).setCompleted(true);
                    DoesList.editTodo(myDoes.get(i));
                } else {
                    myViewHolder.itemView.setBackground(myViewHolder.itemView.getContext().getDrawable(R.drawable.bgitemdoes));
                    myDoes.get(i).setCompleted(false);
                    DoesList.editTodo(myDoes.get(i));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titledoes, descdoes, datedoes;
        CheckBox checkBox;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            titledoes = (TextView) itemView.findViewById(R.id.titledoes);
            descdoes = (TextView) itemView.findViewById(R.id.descdoes);
            datedoes = (TextView) itemView.findViewById(R.id.dateStart);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

}
