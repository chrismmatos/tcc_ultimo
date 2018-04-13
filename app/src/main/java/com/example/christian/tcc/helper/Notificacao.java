package com.example.christian.tcc.helper;

import android.os.AsyncTask;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Notificacao {

    public static final String SERVER_KEY = "AAAA7skqMcQ:APA91bFNM_stckhqzLOVd6xDTJ1jCvRHJ2oTPrl0W0GXTWswTx5uBNSRjTKyFUu9UKy0Hb6wZGcw1i7lA9CvQVvVNkqJ50QU6qMNOX0iXMu0P6Jf7cmOPteQwmHBqcxOv3eugJ8Nj_eh";


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static void sendNotification(final String regToken, final JSONObject dataNotification) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json  =new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    JSONObject ttl = new JSONObject();
                    ttl.put("ttl","60s");
                    dataJson.put("body","Hi this is sent from device to device");
                    dataJson.put("title","dummy title");
                    //json.put("notification",dataJson);
                    json.put("to",regToken);
                    json.put("data", dataNotification);
                    json.put ("android",ttl);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key="+SERVER_KEY)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                    //Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();

    }
}
