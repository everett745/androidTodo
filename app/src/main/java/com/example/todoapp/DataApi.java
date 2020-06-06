package com.example.todoapp;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DataApi {

    //AsyncTask<Void,Void,String> task;


    public static void getUserTodo() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://195.133.196.6:2000/" + LoginApi.getLogin())
                .header("Authorization", "Basic " + LoginApi.getPassword())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.d("TAGT", "ERROR DataApi getUser  " + e.toString());
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {

                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    JSONArray Jarray = Jobject.getJSONArray("events");

                    ArrayList<MyDoes> list = new ArrayList<MyDoes>();
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);

                        MyDoes item = new MyDoes((String) object.get("id"), object.get("startTime").toString(), object.get("endTime").toString(), (String) object.get("title"), (String) object.get("description"));
                        list.add(item);
                    }

                    DoesList.verifyData(list);

                } catch (Throwable e) {
                    Log.d("TAGT", "ERROR " + e.toString());
                }
            }
        });
    }


    /*public void getUserTodo() {
        if (task == null){
            task = new GetUserTask();
            task.execute();
            return;
        }

        while (task.getStatus() != AsyncTask.Status.FINISHED) {}
        task = new GetUserTask();
        task.execute();
    }



    static class GetUserTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://195.133.196.6:2000/" + LoginApi.getLogin())
                    .header("Authorization", "Basic " + LoginApi.getPassword())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            if (!jsonData.isEmpty()) {
                try {
                    JSONObject Jobject = new JSONObject(jsonData);
                    JSONArray Jarray = Jobject.getJSONArray("events");

                    ArrayList<MyDoes> list = new ArrayList<MyDoes>();
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);

                        MyDoes item = new MyDoes((String) object.get("id"), object.get("startTime").toString(), object.get("endTime").toString(), (String) object.get("title"), (String) object.get("description"));
                        list.add(item);
                    }

                    DoesList.verifyData(list);
                } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/


    public static void addEvent(MyDoes event) {
        OkHttpClient client = new OkHttpClient();

        JSONObject Jobject = new JSONObject();
        try {
            Jobject.put("start_time", "4234235132");
            Jobject.put("end_time", event.getEnd_time());
            Jobject.put("title", event.getTitle());
            Jobject.put("description", event.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, Jobject.toString());

        Request request = new Request.Builder()
                .url("http://195.133.196.6:2000/" + LoginApi.getLogin() + "/event")
                .put(body) // PUT here.
                .header("Authorization", "Basic " + LoginApi.getPassword())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.d("TAGT", "ERROR DataApiADD: " + e);
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    try {
                        String jsonData = response.body().string();
                        JSONObject Jobject = new JSONObject(jsonData);
                        String serverId = Jobject.getString("id");

                        DoesList.editTodoId(event, serverId);

                    } catch (Throwable e) {
                        Log.d("TAGT", "onResponse: " + e.toString());
                    }
                }
            }
        });
    }

    public static void removeEvent(MyDoes event) {
        OkHttpClient client = new OkHttpClient();

        JSONObject Jobject = new JSONObject();
        try {
            Jobject.put("id", Integer.parseInt(event.getId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, Jobject.toString());

        Request request = new Request.Builder()
                .url("http://195.133.196.6:2000/" + LoginApi.getLogin() + "/event")
                .delete(body) // PUT here.
                .header("Authorization", "Basic " + LoginApi.getPassword())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.d("TAGT", "ERROR : " + e);
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                }
            }
        });
    }

    public static void editTodo(MyDoes event) {
        OkHttpClient client = new OkHttpClient();

        JSONObject Jobject = new JSONObject();
        try {
            Jobject.put("id", event.getId());
            Jobject.put("start_time", "324234234");
            Jobject.put("end_time", event.getEnd_time());
            Jobject.put("title", event.getTitle());
            Jobject.put("description", event.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, Jobject.toString());

        Request request = new Request.Builder()
                .url("http://195.133.196.6:2000/" + LoginApi.getLogin() + "/event")
                .post(body) // PUT here.
                .header("Authorization", "Basic " + LoginApi.getPassword())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.d("TAGT", "ERROR : " + e);
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                }
            }
        });
    }

}
