package com.example.christian.tcc.activitys.agente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.christian.tcc.R;
import com.example.christian.tcc.helper.Notificacao;

import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;

public class NotificationTransitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_transition);

        if(!Notificacao.isAceito(NotificationTransitionActivity.this)){
            Intent aceitar = new Intent(NotificationTransitionActivity.this, AgenteAcompActivity.class);
            aceitar.putExtra("usuario",usuarioLogado);
            finish();
            startActivity(aceitar);
        }

        else {
            Intent recusar = new Intent(NotificationTransitionActivity.this, AgenteMainActivity.class);
            startActivity(recusar);
            finish();
            Toast.makeText(getApplicationContext(),
                    "Esse pedido não está mais ativo!", Toast.LENGTH_LONG).show();
        }
    }
}
