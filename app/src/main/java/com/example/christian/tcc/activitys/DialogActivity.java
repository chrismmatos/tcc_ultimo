package com.example.christian.tcc.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.christian.tcc.R;
import com.example.christian.tcc.helper.Notificacao;

import static com.example.christian.tcc.config.MyFirebaseMessagingService.dataMap;

public class DialogActivity extends AppCompatActivity {
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        //configurar dialog
        dialog = new AlertDialog.Builder(this);

        // dialog.setView(inflater.inflate(R.layout.layout_dialog,null));
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
                        if(!Notificacao.isAceito(DialogActivity.this)){
                            Intent aceitar = new Intent(DialogActivity.this, AcompAgenteActivity.class);
                            finish();
                            startActivity(aceitar);
                        }

                        else {
                            Intent recusar = new Intent(DialogActivity.this, AgenteMainActivity.class);
                            startActivity(recusar);
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Esse pedido não está mais ativo!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        AlertDialog alert = dialog.create();
        alert.show();

        Button btnPositive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        layoutParams.leftMargin = 10;
        LinearLayout.LayoutParams layoutParamsNegative = (LinearLayout.LayoutParams) btnNegative.getLayoutParams();
        layoutParamsNegative.weight = 10;
        layoutParamsNegative.rightMargin = 10;

        btnPositive.setBackground(getResources().getDrawable(R.drawable.selector_button));
        btnPositive.setTextColor(Color.WHITE);
        btnNegative.setBackground(getResources().getDrawable(R.drawable.selector_button));
        btnNegative.setTextColor(Color.WHITE);

        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParamsNegative);

    }
}
