package com.example.todoapp;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataApi {

    private static DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("does");
    private static DoesAdapter doesAdapter;


    public static ArrayList<MyDoes> getToDos(final Context context) {
        final ArrayList<MyDoes> list = new ArrayList<MyDoes>();

        reference.addValueEventListener(new ValueEventListener() {
             public void onDataChange(DataSnapshot dataSnapshot) {
                // set code to retrive data and replace layout
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    MyDoes p = dataSnapshot1.getValue(MyDoes.class);
                    list.add(p);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        return list;
    }

    public static void addToDo(final MyDoes todo, final Activity act) {
        DatabaseReference ref = reference.child("do" + todo.getKeydoes());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("titledoes").setValue(todo.getTitledoes());
                dataSnapshot.getRef().child("descdoes").setValue(todo.getDescdoes());
                dataSnapshot.getRef().child("datedoes").setValue(todo.getDatedoes());
                dataSnapshot.getRef().child("keydoes").setValue(todo.getTitledoes());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void editToDo(final MyDoes todo, final Activity act) {
        DatabaseReference ref = reference.child("do" + todo.getKeydoes());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("titledoes").setValue(todo.getTitledoes());
                dataSnapshot.getRef().child("descdoes").setValue(todo.getDescdoes());
                dataSnapshot.getRef().child("datedoes").setValue(todo.getDatedoes());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected boolean isOnline(Context context) {
        ConnectivityManager cm =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.isConnectedOrConnecting();
    }

}
