package com.example.christian.tcc.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.christian.tcc.R;
import com.example.christian.tcc.config.CustomFirebaseInstanceIDService;
import com.example.christian.tcc.config.MyFirebaseMessagingService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.example.christian.tcc.activitys.MainAct.usuarioLogado;

public class AgenteMainActivity extends AppCompatActivity {

    private Button btnReceberPedidos;
    private Button btnSolicitarApoio;
    private Spinner spnTipoAgente;
    private TextView txtReceberPedidos;
    private boolean clicou = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agente_main);

        btnReceberPedidos = (Button) findViewById(R.id.btn_receber_pedidos);
        btnSolicitarApoio = (Button) findViewById(R.id.btn_solicitar_apoio);
        spnTipoAgente     = (Spinner) findViewById(R.id.spinner_agente);
        txtReceberPedidos = (TextView) findViewById(R.id.txt_receber_pedidos);
        txtReceberPedidos.setText("Você poderá receber um pedido de acompanhamento a qualquer momento.");


        btnReceberPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receberPedidos();
            }
        });

    }


    public void receberPedidos(){
        if (!clicou){
            String token  = FirebaseInstanceId.getInstance().getToken();
            if(usuarioLogado!=null) {
                usuarioLogado.setToken(token);
                usuarioLogado.salvar();
            }
            FirebaseMessaging.getInstance().subscribeToTopic("agentes");
            btnReceberPedidos.setText("DESATIVAR PEDIDOS");
            txtReceberPedidos.setText("Você poderá receber um pedido de acompanhamento a qualquer momento.");
            clicou = true;
        }

        else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("agentes");
            usuarioLogado.setToken("");
            usuarioLogado.salvar();
            btnReceberPedidos.setText("RECEBER PEDIDOS");
            txtReceberPedidos.setText("Você não está disponível para receber pedidos.");
            clicou = false;

        }

    }


}
