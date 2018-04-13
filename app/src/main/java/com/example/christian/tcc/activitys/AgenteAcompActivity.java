package com.example.christian.tcc.activitys;

import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.christian.tcc.R;

public class AgenteAcompActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agente_acomp);
        NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.cancelAll();

    }
}
