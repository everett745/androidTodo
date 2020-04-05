package com.example.todoapp;

import android.util.Log;

public class MyDoes {

    String title;
    String description;
    String start_time;
    String end_time;
    String id;
    Boolean completed;

    public MyDoes(String id, String start_time, String end_time, String title, String description) {
        Log.d("TAG", "MyDoes   " + id + " , " +  start_time+ " , " + end_time + " , " + title + " , " + description);

        this.id = id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.title = title;
        this.description = description;

        this.completed = false;
    }


    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public String getStart_time() {return start_time;}

    public void setStart_time(String start_time) {this.start_time = start_time;}

    public String getEnd_time() {return end_time;}

    public void setEnd_time(String end_time) {this.end_time = end_time;}

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public Boolean getCompleted() {return completed;}

    public void setCompleted(Boolean completed) {this.completed = completed;}

    public String getJsonString() {
        String rez = "{\"do" + id + "\" : {" +
                    " \"datedoes\" : \"" + start_time + "\", " +
                    " \"descdoes\" : \"" + description + "\", " +
                    " \"keydoes\" : \"" + id + "\", " +
                    " \"titledoes\" : \"" + title + "\"" +
                "}";
        return rez;
    }
}
