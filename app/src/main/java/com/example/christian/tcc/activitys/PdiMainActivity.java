package com.example.christian.tcc.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.christian.tcc.R;
import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PdiMainActivity extends AppCompatActivity {

    public static final String ADRESS ="https://fcm.googleapis.com/fcm/send";

    public static final String SERVER_KEY = "AAAA7skqMcQ:APA91bFNM_stckhqzLOVd6xDTJ1jCvRHJ2oTPrl0W0GXTWswTx5uBNSRjTKyFUu9UKy0Hb6wZGcw1i7lA9CvQVvVNkqJ50QU6qMNOX0iXMu0P6Jf7cmOPteQwmHBqcxOv3eugJ8Nj_eh";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private FirebaseAuth mAuth;
    private Button btnPa;
    OkHttpClient mClient = new OkHttpClient();

    private String SENDER_ID = "ezaLVz0Mh6U:APA91bHFsMVQMKRtG5m-1ncht1Bigb6C-7Bn6x64Se8prX8yINUKQlwc63IrjN3DTHP_ZabUCOumM--OOvlm0_Jau6V2F8o34WvMgnCGtY7Mi8HnHHH74cZCiiA21gP4b9GsuSkwE-V8";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_pdi_main);

        mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        btnPa = (Button)findViewById(R.id.btn_solicitarPA);

        btnPa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviaPa();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void enviaPa()  {
        DatabaseReference  mRoot = ConfiguracaoFirebase.getFirebaseDatabase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pdi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sair) {
            mAuth.signOut();
            startActivity(new Intent(this, LoginAct.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
