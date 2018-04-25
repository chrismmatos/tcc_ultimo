package com.example.christian.tcc.activitys.agente;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.tcc.R;
import com.example.christian.tcc.activitys.LoginAct;
import com.example.christian.tcc.config.ChecaSegundoPlano;
import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.example.christian.tcc.config.CustomFirebaseInstanceIDService;
import com.example.christian.tcc.config.MyFirebaseMessagingService;
import com.example.christian.tcc.helper.Notificacao;
import com.example.christian.tcc.modelo.PedidoAcompanhamento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;

import static com.example.christian.tcc.activitys.LoginAct.mRootRef;
import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;
import static com.example.christian.tcc.helper.Notificacao.sendNotification;

public class AgenteMainActivity extends AppCompatActivity {
    OkHttpClient mClient = new OkHttpClient();
    JSONObject dataNotification;

    private Button btnReceberPedidos;
    private Button btnSolicitarApoio;
    private Spinner spnTipoAgente;
    private TextView txtReceberPedidos;
    private FirebaseAuth mAuth;
    private boolean clicou = true;
    private String tipoAgente ="";
    private PedidoAcompanhamento pedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agente_main);

        NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.cancelAll();
        mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        btnSolicitarApoio = (Button) findViewById(R.id.btn_solicitar_apoio);
        btnReceberPedidos = (Button) findViewById(R.id.btn_receber_pedidos);
        spnTipoAgente     = (Spinner) findViewById(R.id.spinner_agente);
        txtReceberPedidos = (TextView) findViewById(R.id.txt_receber_pedidos);
        txtReceberPedidos.setText("Você poderá receber um pedido de acompanhamento a qualquer momento.");
        btnReceberPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receberPedidos();
            }
        });
        btnSolicitarApoio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitaApoio(v);
            }
        });

        pedirPermissoes();
    }

    public void solicitaApoio(View v){

        spnTipoAgente.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoAgente = spnTipoAgente.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pedido = new PedidoAcompanhamento();
        String idPedido = mRootRef.child("pedidos").push().getKey();

        pedido.setId(idPedido);
        pedido.setUsuario(usuarioLogado.getId());
        buscaEndereco();
        pedido.setData(Notificacao.retornaHora());
        pedido.setTipo("Acompanhamento");
        pedido.salvar();

        dataNotification = new JSONObject();
        try {
            dataNotification.put("usuario",pedido.getUsuario());
            dataNotification.put("id",pedido.getId());
            dataNotification.put("descricao", usuarioLogado.getNome() + " está solicitando um acompanhamento!");
            dataNotification.put("titulo", "Pedido de Acompannhamento");
            dataNotification.put("localizacao", pedido.getLocalizacao());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendNotification("topics/"+tipoAgente,dataNotification);
    }

    public void receberPedidos(){
        if (!clicou){
            String token  = FirebaseInstanceId.getInstance().getToken();
            if(usuarioLogado!=null) {
                usuarioLogado.setToken(token);
                usuarioLogado.salvar();
            }
            FirebaseMessaging.getInstance().subscribeToTopic("GuardaMunicipal");
            btnReceberPedidos.setText("DESATIVAR PEDIDOS");
            txtReceberPedidos.setText("Você poderá receber um pedido de acompanhamento a qualquer momento.");
            clicou = true;
        }
        else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("GuardaMunicipal");
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
            FirebaseMessaging.getInstance().unsubscribeFromTopic("GuardaMunicipal");
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

    private void pedirPermissoes() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
            configurarServico();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configurarServico();
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void configurarServico(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    atualizar(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) { }

                public void onProviderEnabled(String provider) { }

                public void onProviderDisabled(String provider) { }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch(SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void atualizar(Location location) {
        Double latPoint = location.getLatitude();
        Double lngPoint = location.getLongitude();

        if(usuarioLogado!=null) {
            usuarioLogado.setLatitude(latPoint);
            usuarioLogado.setLongitude(lngPoint);
            usuarioLogado.salvar();
        }
    }
    public void buscaEndereco(){
        String rua = null;
        String address = null;

        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(usuarioLogado.getLatitude(), usuarioLogado.getLongitude(),1);
            rua = addresses.get(0).getThoroughfare();
            address = addresses.get(0).getAddressLine(0);// rua numero e bairro
            System.out.println("Endereço " + address);
            pedido.setLocalizacao(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
