package com.example.christian.tcc.activitys;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.christian.tcc.R;
import com.example.christian.tcc.config.ChecaSegundoPlano;
import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.example.christian.tcc.config.CustomFirebaseInstanceIDService;
import com.example.christian.tcc.config.MyFirebaseMessagingService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;

public class AgenteMainActivity extends AppCompatActivity {

    private Button btnReceberPedidos;
    private Button btnSolicitarApoio;
    private Spinner spnTipoAgente;
    private TextView txtReceberPedidos;
    private FirebaseAuth mAuth;
    private boolean clicou = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agente_main);

        NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.cancelAll();

        mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("agentes");
            usuarioLogado.setToken("");
            usuarioLogado.salvar();
            mAuth.signOut();
            startActivity(new Intent(this, LoginAct.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChecaSegundoPlano.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChecaSegundoPlano.activityPaused();
    }


}
