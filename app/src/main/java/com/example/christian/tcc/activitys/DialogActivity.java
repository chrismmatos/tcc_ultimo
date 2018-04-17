package com.example.christian.tcc.activitys;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.christian.tcc.R;

import static com.example.christian.tcc.config.MyFirebaseMessagingService.dataMap;

public class DialogActivity extends AppCompatActivity {
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        //configurar dialog
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle(dataMap.get("titulo"));
        //configurar mensagem
        dialog.setMessage(dataMap.get("descricao"));
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.ic_accessible);

        dialog.setNegativeButton("Recusar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent recusar = new Intent(DialogActivity.this, AgenteMainActivity.class);
                        startActivity(recusar);
                        finish();
                    }
                });

        dialog.setPositiveButton("Aceitar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent aceitar = new Intent(DialogActivity.this, AgenteAcompActivity.class);
                        startActivity(aceitar);
                        finish();
                    }
                });

        dialog.show();

        System.out.println("está rodadendo");
    }
}
