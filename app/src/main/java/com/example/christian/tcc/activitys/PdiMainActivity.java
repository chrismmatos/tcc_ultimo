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


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.christian.tcc.activitys.MainAct.usuarioLogado;

public class PdiMainActivity extends AppCompatActivity {

    public static final String ADRESS ="https://fcm.googleapis.com/fcm/send";

    public static final String SERVER_KEY = "AAAA7skqMcQ:APA91bFNM_stckhqzLOVd6xDTJ1jCvRHJ2oTPrl0W0GXTWswTx5uBNSRjTKyFUu9UKy0Hb6wZGcw1i7lA9CvQVvVNkqJ50QU6qMNOX0iXMu0P6Jf7cmOPteQwmHBqcxOv3eugJ8Nj_eh";

    private FirebaseAuth mAuth;
    private Button btnPa;
    OkHttpClient mClient = new OkHttpClient();

    private String SENDER_ID = "cU1VUF5EAms:APA91bFH7WQ7dYJGmmM16aRjCUmBMPzA28OT9R8VTI5Z2O6sekFXOR9CuHli0C-qZkEpPm-vWgJYGayDuuzdxAUh4pkZ1hVu9na2CV2dTheL81FyBWm6uzyq0gQujwIPdkJBgSI8r9R7";



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

        sendNotification(SENDER_ID);
//        DatabaseReference  mRoot = ConfiguracaoFirebase.getFirebaseDatabase();
//
//        Map pedido = new HashMap<>();
//        pedido.put("origem", usuarioLogado.getNome());
//        pedido.put("token", SENDER_ID);
//        pedido.put("destino", "agente teste");



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

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private void sendNotification(final String regToken) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject dataJson=new JSONObject();
                    dataJson.put("body","Hi this is sent from device to device");
                    dataJson.put("title","dummy title");
                    json.put("notification",dataJson);
                    json.put("to",regToken);
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
