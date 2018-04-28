package com.example.christian.tcc.activitys.agente;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.tcc.R;
import com.example.christian.tcc.activitys.LoginAct;
import com.example.christian.tcc.activitys.pdi.PdiAcompActivity;
import com.example.christian.tcc.activitys.pdi.PdiMainActivity;
import com.example.christian.tcc.config.ChecaSegundoPlano;
import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.example.christian.tcc.config.CustomFirebaseInstanceIDService;
import com.example.christian.tcc.config.MyFirebaseMessagingService;
import com.example.christian.tcc.helper.Notificacao;
import com.example.christian.tcc.modelo.PedidoAcompanhamento;
import com.example.christian.tcc.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;

import static com.example.christian.tcc.activitys.LoginAct.mRootRef;
import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;
import static com.example.christian.tcc.helper.Notificacao.sendNotification;

public class AgenteMainActivity extends AppCompatActivity {
    OkHttpClient mClient = new OkHttpClient();
    JSONObject dataNotification;

    //tempo de espera em minutos
    private final int TEMPO_ESPERA = 2;

    private Button btnReceberPedidos;
    private Button btnSolicitarApoio;
    private Spinner spnTipoAgente;
    private TextView txtReceberPedidos;
    private TextView txtMapearAgentes;
    private TextView txtNome;
    private TextView txtTipoAgente;
    private ImageView imgAvatar;
    private TextView txtApoio;
    private FirebaseAuth mAuth;
    private boolean clicou = true;
    private String tipoAgente ="";
    private PedidoAcompanhamento pedido;
    private DatabaseReference refUsers;
    private ValueEventListener pedidoListener;
    private  DatabaseReference refPedido;
    private Usuario usuario;
    static public AlertDialog alerta;
    private MyCountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agente_main);

        NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.cancelAll();
        mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        refUsers = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");


        btnSolicitarApoio = (Button) findViewById(R.id.btn_solicitar_apoio);
        btnReceberPedidos = (Button) findViewById(R.id.btn_receber_pedidos);
        spnTipoAgente     = (Spinner) findViewById(R.id.spinner_agente);
        txtReceberPedidos = (TextView) findViewById(R.id.txt_receber_pedidos);
        txtMapearAgentes = (TextView) findViewById(R.id.txt_mapear);
        txtNome = (TextView) findViewById(R.id.txt_nome);
        txtTipoAgente = (TextView) findViewById(R.id.txt_agente);
        txtApoio = (TextView) findViewById(R.id.txt_apoio);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        txtApoio.setText("Selecione uma categoria de agentes para solicitar um apoio.");
        txtMapearAgentes.setText("Encontre agentes próximos.");
        txtReceberPedidos.setText("Disponível para receber pedidos.");
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
        spnTipoAgente.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoAgente = spnTipoAgente.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        carregaDados();
        pedirPermissoes();
    }

    private void carregaDados(){
        switch (usuarioLogado.getTipoAgente()){
            case "Guarda Municipal":
                imgAvatar.setImageDrawable(getDrawable(R.drawable.ic_guarda));
                break;
            case "Agente de Defesa Civil":
                imgAvatar.setImageDrawable(getDrawable(R.drawable.ic_defesa_civil));
                break;
            case "Policial Militar":
                imgAvatar.setImageDrawable(getDrawable(R.drawable.ic_polical));
                break;
            case "Bombeiro":
                imgAvatar.setImageDrawable(getDrawable(R.drawable.ic_bombeiro));
                break;

                default:
                    imgAvatar.setImageDrawable(getDrawable(R.drawable.ic_saude));
        }
        txtTipoAgente.setText(usuarioLogado.getTipoAgente());
        txtNome.setText(usuarioLogado.getNome());
    }

    public void criaDialog(){
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li = getLayoutInflater();

        //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.dialog_layout, null);
        TextView textView = view.findViewById(R.id.tv_timer);
        timer = new MyCountDownTimer(this, textView,  TEMPO_ESPERA *60*1000, 1000);
        timer.start();
        //definimos para o botão do layout um clickListener
        view.findViewById(R.id.btn_cancelar).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                cancelaPedido();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pedido enviado!");
        builder.setView(view);
        builder.setCancelable(false);
        alerta = builder.create();
        alerta.show();
    }

    public void solicitaApoio(View v){
        pedido = new PedidoAcompanhamento();
        String idPedido = mRootRef.child("pedidos").push().getKey();

        pedido.setId(idPedido);
        pedido.setUsuario(usuarioLogado.getId());
        buscaEndereco();
        pedido.setData(Notificacao.retornaHora());
        pedido.setTipo("Reforço");
        pedido.salvar();
        verificaPedido();

        dataNotification = new JSONObject();
        try {
            dataNotification.put("usuario",pedido.getUsuario());
            dataNotification.put("id",pedido.getId());
            dataNotification.put("descricao", usuarioLogado.getTipoAgente()+ " "+usuarioLogado.getNome() + " está solicitando reforço!");
            dataNotification.put("titulo", "Pedido de Reforço");
            dataNotification.put("localizacao", pedido.getLocalizacao());
            dataNotification.put("tipo", pedido.getTipo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        localizaAgentes();
        criaDialog();
    }

    void verificaPedido(){
        refPedido= mRootRef.child("pedidos").child(pedido.getId());

        pedidoListener = refPedido.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pedido = dataSnapshot.getValue(PedidoAcompanhamento.class);

                if(pedido.isAtivo()) {
                    refPedido.removeEventListener(pedidoListener);
                    alerta.dismiss();
                    timer.cancel();
                    final Intent i = new Intent(AgenteMainActivity.this, AgenteAcompActivity.class);
                    i.putExtra("pedido",pedido);
                    i.putExtra("usuario", usuarioLogado);

                    refPedido = mRootRef.child("usuarios").child(pedido.getAcompanhante());

                    refPedido.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario acompanhante = dataSnapshot.getValue(Usuario.class);
                            i.putExtra("acompanhante",acompanhante);
                            startActivity(i);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void localizaAgentes(){
        Query userQuery = refUsers.orderByChild("tipoAgente").equalTo(tipoAgente);

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()){
                    usuario = usuarioSnapshot.getValue(Usuario.class);
                    if(!usuarioLogado.getId().equals(usuario.getId()))
                        Notificacao.sendNotification(usuario.getToken(), dataNotification);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        System.out.println("teste");
    }

    public void receberPedidos(){
        if (!clicou){
            String token  = FirebaseInstanceId.getInstance().getToken();
            if(usuarioLogado!=null) {
                usuarioLogado.setToken(token);
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
            FirebaseMessaging.getInstance().unsubscribeFromTopic("agente");
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
    public void buscaEndereco() {
        String rua = null;
        String address = null;

        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(usuarioLogado.getLatitude(), usuarioLogado.getLongitude(), 1);
            rua = addresses.get(0).getThoroughfare();
            address = addresses.get(0).getAddressLine(0);// rua numero e bairro
            System.out.println("Endereço " + address);
            pedido.setLocalizacao(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void expiraPedido(){
        android.app.AlertDialog.Builder dialog;

        //configurar dialog
        dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("Tempo limite atingido!");
        //configurar mensagem
        dialog.setMessage("O tempo de limite de espera foi atingido. Você pode realizar o pedido novamente agora ou mais tarde");
        dialog.setCancelable(false);
        dialog.setNeutralButton("OK",null);

        android.app.AlertDialog alert = dialog.create();
        alert.show();

        Button btnNeutral = alert.getButton(android.app.AlertDialog.BUTTON_NEUTRAL);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnNeutral.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.weight = 40;
        btnNeutral.setBackground(getResources().getDrawable(R.drawable.selector_button));
        btnNeutral.setTextColor(Color.WHITE);
        btnNeutral.setLayoutParams(layoutParams);
    }

    // Classe Contadora
    public class MyCountDownTimer extends CountDownTimer {

        private TextView tv;
        private Context context;
        private long timerInTure;

        public MyCountDownTimer(Context context, TextView tv, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.context = context;
            this.tv = tv;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerInTure = millisUntilFinished;
            tv.setText("Procurando agentes próximos...\n"
                    + getCorrectTimer(true, millisUntilFinished) + ":" + getCorrectTimer(false, millisUntilFinished));
        }

        @Override
        public void onFinish() {
            timerInTure -= 1000;
            tv.setText("Procurando agentes próximos...\n"
                    + getCorrectTimer(true, timerInTure) + ":" + getCorrectTimer(false, timerInTure));
            alerta.dismiss();
            cancelaPedido();
            expiraPedido();
        }

        private String getCorrectTimer(boolean isMinute, long millisUntilFinished) {
            String aux;
            int constCalendar = isMinute ? Calendar.MINUTE : Calendar.SECOND;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millisUntilFinished);
            aux = c.get(constCalendar) < 10 ? "0" + c.get(constCalendar) : "" + c.get(constCalendar);
            return (aux);
        }
    }

    public void cancelaPedido () {
        pedido.setAtivo(true);
        pedido.salvar();
        refPedido.removeEventListener(pedidoListener);
        refPedido.removeValue();
        alerta.dismiss();
        timer.cancel();
    }


}
