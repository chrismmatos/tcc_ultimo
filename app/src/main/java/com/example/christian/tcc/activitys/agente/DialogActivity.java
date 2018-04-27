package com.example.christian.tcc.activitys.agente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.christian.tcc.R;
import com.example.christian.tcc.helper.Notificacao;

import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;
import static com.example.christian.tcc.config.MyFirebaseMessagingService.dataMap;
import static com.example.christian.tcc.config.MyFirebaseMessagingService.pedidoAtual;

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

        switch (pedidoAtual.getTipo()){
            case "Reforço" : {
                dialog.setIcon(R.drawable.ic_siren);
                break;
            }
            default:
                dialog.setIcon(R.drawable.ic_accessible);
        }

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
                            Intent aceitar = new Intent(DialogActivity.this, AgenteAcompActivity.class);
                            aceitar.putExtra("usuario",usuarioLogado);
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
