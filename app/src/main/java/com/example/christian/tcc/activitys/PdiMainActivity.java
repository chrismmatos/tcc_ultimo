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
import com.example.christian.tcc.config.ChecaSegundoPlano;
import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.example.christian.tcc.helper.Notificacao;
import com.example.christian.tcc.modelo.PedidoAcompanhamento;
import com.example.christian.tcc.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import okhttp3.OkHttpClient;


import static com.example.christian.tcc.activitys.LoginAct.mRootRef;
import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;
import static com.example.christian.tcc.helper.Notificacao.sendNotification;

public class PdiMainActivity extends AppCompatActivity {

    public static final String ADRESS ="https://fcm.googleapis.com/fcm/send";

    private FirebaseAuth mAuth;
    private Button btnPa;

    OkHttpClient mClient = new OkHttpClient();

    JSONObject dataNotification;

    private DatabaseReference mUserRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");

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

        PedidoAcompanhamento pedido = new PedidoAcompanhamento();
        String idPedido = mRootRef.child("pedidos").push().getKey();

        pedido.setId(idPedido);
        pedido.setUsuario(usuarioLogado.getId());
        pedido.salvar();

       dataNotification = new JSONObject();
        try {
            dataNotification.put("usuario",pedido.getUsuario());
            dataNotification.put("id",pedido.getId());
            dataNotification.put("descricao", usuarioLogado.getNome() + " está solicitando um acompanhamento!");
            dataNotification.put("titulo", "Pedido de Acompannhamento");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        Query query = mUserRef.orderByChild("tipoUsuario").equalTo("Agente").limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null) {
                    DataSnapshot child = dataSnapshot.getChildren().iterator().next();
                    Usuario usuarioNotificado = child.getValue(Usuario.class);
                    sendNotification(usuarioNotificado.getToken(),dataNotification);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Se ocorrer um erro
            }
        });


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
