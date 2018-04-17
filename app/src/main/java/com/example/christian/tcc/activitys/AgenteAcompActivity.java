package com.example.christian.tcc.activitys;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.christian.tcc.R;
import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.example.christian.tcc.modelo.PedidoAcompanhamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;
import static com.example.christian.tcc.config.MyFirebaseMessagingService.pedidoAtual;


public class AgenteAcompActivity extends AppCompatActivity {

    DatabaseReference refPedido = ConfiguracaoFirebase.getFirebaseDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agente_acomp);

        NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.cancelAll();

        if(pedidoAtual!=null)
         System.out.println(pedidoAtual);

        String caminho = "pedidos/"+pedidoAtual.getId();

        refPedido = refPedido.child(caminho);

        refPedido.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {

                    if (!pedidoAtual.isIniciado()) {
                        pedidoAtual.setAcompanhante(usuarioLogado.getId());
                        pedidoAtual.setIniciado(true);
                        pedidoAtual.salvar();

                    } else {
                        Intent i = new Intent(AgenteAcompActivity.this, AgenteMainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }
}
