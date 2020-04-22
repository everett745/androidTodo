package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.HttpGet;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.HttpResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.http.GET;


public class MainActivity extends AppCompatActivity {

    TextView titlepage, subtitlepage;
    View btnAddNew;

    RecyclerView ourdoes;
    DoesAdapter doesAdapter;
    DoesList dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sh = getSharedPreferences("user", MODE_APPEND);
        String login = sh.getString("login", "");
        String psw = sh.getString("psw", "");

        if (login == null || psw == null) {
            Intent a = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(a);
        } else {
            LoginApi.setLogin(login);
            LoginApi.setPassword(psw);
        }

        titlepage = findViewById(R.id.titlepage);
        subtitlepage = findViewById(R.id.subtitlepage);

        btnAddNew = findViewById(R.id.btnAddNew);

        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        titlepage.setTypeface(MMedium);
        subtitlepage.setTypeface(MLight);

        //btnAddNew.setTypeface(MLight);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, NewTaskAct.class);
                startActivity(a);
            }
        });


        // working with data
        ourdoes = findViewById(R.id.ourdoes);
        ourdoes.setLayoutManager(new LinearLayoutManager(this));
        
        // get toDos
        dl = new DoesList(MainActivity.this);

        doesAdapter = new DoesAdapter(MainActivity.this, DoesList.getList());
        ourdoes.setAdapter(doesAdapter);
        doesAdapter.notifyDataSetChanged();
    }
}

/*public class SendPostRequest extends AsyncTask<String, Void, String> {
    protected void onPreExecute(){}
    protected String doInBackground(String... arg0) {
        try {
            URL url = new URL("https://studytutorial.in/post.php");
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("name", "abc");
            postDataParams.put("email", "abc@gmail.com");
            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 *//* milliseconds *//*);
            conn.setConnectTimeout(15000 *//* milliseconds *//*);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            return conn.getResponseMessage().toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {}
}*/
















