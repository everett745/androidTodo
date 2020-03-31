package com.example.todoapp;

public class MyDoes {

    String titledoes;
    String datedoes;
    String descdoes;
    String keydoes;
    Boolean completed;

    public MyDoes(String titledoes, String datedoes, String descdoes, String keydoes, Boolean completed) {
        this.titledoes = titledoes;
        this.datedoes = datedoes;
        this.descdoes = descdoes;
        this.keydoes = keydoes;
        this.completed = completed;
    }
    public MyDoes(String titledoes, String datedoes, String descdoes, String keydoes) {
        this.titledoes = titledoes;
        this.datedoes = datedoes;
        this.descdoes = descdoes;
        this.keydoes = keydoes;
        this.completed = false;
    }



    public String getKeydoes() {
        return keydoes;
    }

    public void setKeydoes(String keydoes) {
        this.keydoes = keydoes;
    }

    public String getTitledoes() {
        return titledoes;
    }

    public void setTitledoes(String titledoes) {
        this.titledoes = titledoes;
    }

    public String getDatedoes() {
        return datedoes;
    }

    public void setDatedoes(String datedoes) {
        this.datedoes = datedoes;
    }

    public String getDescdoes() {
        return descdoes;
    }

    public void setDescdoes(String descdoes) {
        this.descdoes = descdoes;
    }

    public Boolean getCompleted() {return completed;}

    public void setCompleted(Boolean completed) {this.completed = completed;}

    public String getJsonString() {
        String rez = "{\"do" + keydoes + "\" : {" +
                    " \"datedoes\" : \"" + datedoes + "\", " +
                    " \"descdoes\" : \"" + descdoes + "\", " +
                    " \"keydoes\" : \"" + keydoes + "\", " +
                    " \"titledoes\" : \"" + titledoes + "\"" +
                "}";
        return rez;
    }
}
