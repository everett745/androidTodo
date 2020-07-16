package com.example.todoapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private TextView titlePage;
    private Button btnAddNew;

    private FloatingActionButton btnCamera;

    private RecyclerView ourdoes;
    private DoesAdapter doesAdapter;
    private DoesList dl;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CameraAPI.requestPermissions(MainActivity.this);
        createNotificationChannel();
        if (checkAuth()) {
            titlePage = findViewById(R.id.titlepage);

            btnAddNew = findViewById(R.id.btnAddNew);
            Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
            Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

            progressBar = findViewById(R.id.progress_bar);

            titlePage.setTypeface(MMedium);
            //btnAddNew.setTypeface(MLight);

            btnAddNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(MainActivity.this, NewTaskAct.class);
                    startActivity(a);
                }
            });


            ourdoes = findViewById(R.id.ourdoes);
            ourdoes.setLayoutManager(new LinearLayoutManager(this));

            // get toDos
            dl = new DoesList(MainActivity.this);

            doesAdapter = new DoesAdapter(MainActivity.this, DoesList.getList());
            ourdoes.setAdapter(doesAdapter);
            doesAdapter.notifyDataSetChanged();
            btnCamera = findViewById(R.id.btnCamera);
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraAPI.requestVideo(MainActivity.this);
                }
            });
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        Uri videoURI = CameraAPI.getVideoUri(requestCode, resultCode, data);
        Cutter decoder = new Cutter();
        decoder.execute(videoURI);
        progressBar.setVisibility(View.VISIBLE);
    }


    class SendTask extends AsyncTask<Bitmap, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Bitmap... pics) {


            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            ByteArrayOutputStream outputStream;
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            for (int i = 0; i < pics.length; i++) {
                outputStream = new ByteArrayOutputStream();
                pics[i].compress(Bitmap.CompressFormat.JPEG, 25, outputStream);
                requestBodyBuilder.addFormDataPart(
                        "image" + i,
                        "image" + i + ".jpg",
                        RequestBody.create(MediaType.parse("image/*jpg"), outputStream.toByteArray()));
            }


            Request request = new Request.Builder()
                    .url("http://192.168.0.109:8080/FunnotesJServer/fatigueTest")
                    .post(requestBodyBuilder.build())
                    .build();


            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String respBody = response.body().string();
                    return new JSONObject(respBody).getBoolean("isTired");

                } else {
                    return null;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            DialogFragment responseFragment;
            if (aBoolean) {
                responseFragment = new TiredDialogFragment();
            } else {
                responseFragment = new RestedDialogFragment();
            }
            responseFragment.show(getSupportFragmentManager(), "Dialog");
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

    class Cutter extends AsyncTask<Uri, Void, Bitmap[]> {
        private int fps = 5;
        private final int second = 1_000_000;


        @Override
        protected Bitmap[] doInBackground(Uri... uris) {
            if (uris == null || uris[0] == null)
                return null;
            int videoLength = 3;
            Bitmap[] frames = new Bitmap[fps * videoLength];
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(MainActivity.this, uris[0]);
            for (int i = 0; i < fps * videoLength; i++) {
                int current_time = second * i / fps;
                frames[i] = retriever.getFrameAtTime(current_time, MediaMetadataRetriever.OPTION_CLOSEST);
            }
            return frames;
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            super.onPostExecute(bitmaps);
            SendTask task = new SendTask();
            task.execute(bitmaps);
        }
    }


    public boolean checkAuth() {
        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);
        String login = sh.getString("login", "");
        String psw = sh.getString("psw", "");
        LoginApi.setLogin(login);
        LoginApi.setPassword(psw);

        if (login.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Для первого запуска необходима авторизация" , Toast.LENGTH_LONG).show();

            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(loginIntent);
        } else {
            LoginApi.setLogin(login);
            LoginApi.setPassword(psw);
            return true;
        }
        return false;
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Напоминание о задачах";
            String description = "Уведомления о предстоящих задачах";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        dl = new DoesList(MainActivity.this);
        doesAdapter = new DoesAdapter(MainActivity.this, DoesList.getList());
        ourdoes.setAdapter(doesAdapter);
        doesAdapter.notifyDataSetChanged();
    }


}

