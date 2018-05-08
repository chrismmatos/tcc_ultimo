package com.example.christian.tcc.activitys.pdi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.tcc.R;
import com.example.christian.tcc.activitys.LoginAct;
import com.example.christian.tcc.config.ChecaSegundoPlano;
import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.example.christian.tcc.helper.Notificacao;
import com.example.christian.tcc.modelo.PedidoAcompanhamento;
import com.example.christian.tcc.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import okhttp3.OkHttpClient;


import static com.example.christian.tcc.activitys.LoginAct.mRootRef;
import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;
import static com.example.christian.tcc.helper.Notificacao.sendNotification;

public class PdiMainActivity extends AppCompatActivity {
    //tempo de espera em minutos
    private final int TEMPO_ESPERA = 2;

    public static final String ADRESS ="https://fcm.googleapis.com/fcm/send";
    private FirebaseAuth mAuth;
    private Button btnPa;
    private Button btnPae;

    OkHttpClient mClient = new OkHttpClient();
    JSONObject dataNotification;

    static public AlertDialog alerta;
    private DatabaseReference mUserRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");
    private PedidoAcompanhamento pedido;
    private ValueEventListener pedidoListener;
    private  DatabaseReference refPedido;
    private String SENDER_ID = "cU1VUF5EAms:APA91bFH7WQ7dYJGmmM16aRjCUmBMPzA28OT9R8VTI5Z2O6sekFXOR9CuHli0C-qZkEpPm-vWgJYGayDuuzdxAUh4pkZ1hVu9na2CV2dTheL81FyBWm6uzyq0gQujwIPdkJBgSI8r9R7";
    private MyCountDownTimer timer;
    private TextView txtNome;
    private String topic;
    private Spinner spnPAE;
    private String paeSelecionado;
    private String tipoUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdi_main);

        spnPAE = (Spinner)findViewById(R.id.spinnerPAE);
        txtNome = (TextView) findViewById(R.id.txt_nome_usuario);
        mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        btnPa = (Button)findViewById(R.id.btn_solicitarPA);
        btnPa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topic = "voluntario";
                tipoUsuario = "voluntários";
                enviaPa();
            }
        });
        btnPae = (Button)findViewById(R.id.btn_solicitarPAE);
        btnPae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipoUsuario = "agentes";
                enviaPa();
            }
        });
        txtNome.setText(usuarioLogado.getNome());

        spnPAE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paeSelecionado = spnPAE.getSelectedItem().toString();

                switch (paeSelecionado){
                    case("Sozinho em local com pouca iluminação"):
                        topic = "guarda";
                        break;

                    case("Mal estar/ Mal súbito"):
                        topic = "samu";
                        break;

                    case("Falta de mobilidade"):
                        topic = "samu";
                        break;

                    case("Perda de memória"):
                        topic = "guarda";
                        break;

                    case("Local com alagamento"):
                        topic = "defesa";
                        break;

                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pedirPermissoes();
    }


    public void criaDialog(){
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li = getLayoutInflater();

        //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.dialog_layout, null);
        TextView textView = view.findViewById(R.id.tv_timer);
        timer = new MyCountDownTimer(this, textView,  TEMPO_ESPERA *60*1000, 1000, tipoUsuario);
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

    public  void cancelaPedido(){
        pedido.setAtivo(true);
        pedido.salvar();
        refPedido.removeEventListener(pedidoListener);
        refPedido.removeValue();
        alerta.dismiss();
        timer.cancel();
    }

    public void enviaPa()  {

        pedido = new PedidoAcompanhamento();
        String idPedido = mRootRef.child("pedidos").push().getKey();

        pedido.setId(idPedido);
        pedido.setUsuario(usuarioLogado.getId());
        buscaEndereco();
        pedido.setData(Notificacao.retornaHora());
        pedido.setTipo("Acompanhamento");
        pedido.salvar();

        verificaPedido();

       dataNotification = new JSONObject();
        try {
            dataNotification.put("usuario",pedido.getUsuario());
            dataNotification.put("id",pedido.getId());
            dataNotification.put("descricao", usuarioLogado.getNome() + " está solicitando um acompanhamento!");
            dataNotification.put("titulo", "Pedido de Acompannhamento");
            dataNotification.put("localizacao", pedido.getLocalizacao());
            dataNotification.put("tipo", pedido.getTipo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Query query = mUserRef.orderByChild("tipoUsuario").equalTo("Agente").limitToFirst(2);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null) {
                    DataSnapshot child = dataSnapshot.getChildren().iterator().next();
                    Usuario usuarioNotificado = child.getValue(Usuario.class);
                    sendNotification("/topics/"+ topic ,dataNotification);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Se ocorrer um erro
            }
        });

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
                    final Intent i = new Intent(PdiMainActivity.this, PdiAcompActivity.class);
                    i.putExtra("pedido",pedido);

                    refPedido = mRootRef.child("usuarios").child(pedido.getAcompanhante());

                    refPedido.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario acompanhante = dataSnapshot.getValue(Usuario.class);
                            i.putExtra("acompanhante",acompanhante);
                            startActivity(i);
                            finish();
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
            System.out.println("Endereço "+ address);
            pedido.setLocalizacao(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Classe Contadora
    public class MyCountDownTimer extends CountDownTimer {

        private TextView tv;
        private Context context;
        private long timerInTure;
        private String tipoUsuario;

        public MyCountDownTimer(Context context, TextView tv, long millisInFuture, long countDownInterval, String tipoUsuario) {
            super(millisInFuture, countDownInterval);
            this.tipoUsuario = tipoUsuario;
            this.context = context;
            this.tv = tv;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerInTure = millisUntilFinished;
            tv.setText( "Procurando " +tipoUsuario +    " próximos...\n"
                    +getCorrectTimer(true, millisUntilFinished)+ ":" + getCorrectTimer(false, millisUntilFinished) );
        }

        @Override
        public void onFinish() {
            timerInTure -= 1000;
            tv.setText( "Procurando " +tipoUsuario +    " próximos ...\n"
                    +getCorrectTimer(true, timerInTure)+ ":" + getCorrectTimer(false, timerInTure) );
            alerta.dismiss();
            cancelaPedido();
            expiraPedido();
        }

        private String getCorrectTimer(boolean isMinute, long millisUntilFinished){
            String aux;
            int constCalendar = isMinute ? Calendar.MINUTE : Calendar.SECOND;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millisUntilFinished);
            aux = c.get(constCalendar) < 10 ? "0" +c.get(constCalendar) : "" + c.get(constCalendar);
            return (aux);
        }
    }


}
