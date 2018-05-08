package com.example.christian.tcc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.christian.tcc.activitys.LoginAct;
import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;

public class VoluntarioActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnReceberPedidos;
    private TextView txtNome;
    private TextView txtLevel;
    private TextView txtReceberPedidos;
    private ProgressBar progressBar;
    private boolean clicou = true;
    private ImageView imgLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voluntario);

        mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        btnReceberPedidos = (Button) findViewById(R.id.btn_receber_pedidos_voluntario);
        txtNome = (TextView) findViewById(R.id.txt_nome_usuario);
        txtLevel = (TextView) findViewById(R.id.txt_level_voluntario);
        txtReceberPedidos = (TextView) findViewById(R.id.txt_receber_pedidos_voluntario);
        progressBar = (ProgressBar) findViewById(R.id.progress_level_voluntario);
        txtReceberPedidos.setText("Disponível para receber pedidos.");
        btnReceberPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receberPedidos();
            }
        });
        txtNome.setText(usuarioLogado.getNome());
        progressBar.setProgress(usuarioLogado.getProgress());
        txtLevel.setText("Level "+ usuarioLogado.getLevel());
        imgLevel = (ImageView) findViewById(R.id.img_level);
    }

    void carregaInsigna(){
        if(usuarioLogado.getLevel()>20)
            imgLevel.setImageResource(R.drawable.ic_level8);
        else if(usuarioLogado.getLevel()>17)
            imgLevel.setImageResource(R.drawable.ic_level7);
        else if(usuarioLogado.getLevel()>14)
            imgLevel.setImageResource(R.drawable.ic_level6);
        else if(usuarioLogado.getLevel()>11)
            imgLevel.setImageResource(R.drawable.ic_level5);
        else if(usuarioLogado.getLevel()>8)
            imgLevel.setImageResource(R.drawable.ic_level4);
        else if(usuarioLogado.getLevel()>5)
            imgLevel.setImageResource(R.drawable.ic_level3);
        else if(usuarioLogado.getLevel()>2)
            imgLevel.setImageResource(R.drawable.ic_level2);
        else  imgLevel.setImageResource(R.drawable.ic_level1);
    }


    public void receberPedidos(){
        if (!clicou){
            String token  = FirebaseInstanceId.getInstance().getToken();
            if(usuarioLogado!=null) {
                usuarioLogado.setToken(token);
                usuarioLogado.setOnline(true);
                usuarioLogado.salvar();
            }
            FirebaseMessaging.getInstance().subscribeToTopic("agente");
            btnReceberPedidos.setText("DESATIVAR PEDIDOS");
            txtReceberPedidos.setText("Disponível para receber pedidos.");
            btnReceberPedidos.setBackground(getResources().getDrawable(R.drawable.selector_button_concluir));
            clicou = true;
        }
        else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("agente");
            usuarioLogado.setToken("");
            usuarioLogado.setOnline(false);
            usuarioLogado.salvar();
            btnReceberPedidos.setText("RECEBER PEDIDOS");
            txtReceberPedidos.setText("Você não está disponível para receber pedidos.");
            btnReceberPedidos.setBackground(getResources().getDrawable(R.drawable.selector_button_cancelar));
            clicou = false;
        }
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
        if (id == R.id.action_exit) {
            usuarioLogado.setOnline(false);
            usuarioLogado.setToken("");
            mAuth.signOut();
            startActivity(new Intent(this, LoginAct.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
