package com.example.christian.tcc.activitys.agente;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.tcc.R;
import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.example.christian.tcc.modelo.PedidoAcompanhamento;
import com.example.christian.tcc.modelo.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.text.DecimalFormat;
import java.util.ArrayList;


import static com.example.christian.tcc.config.MyFirebaseMessagingService.pedidoAtual;

public class AgenteAcompActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private TextView tvDescricao;
    private TextView tvDistancia;
    private TextView tvData;
    private TextView tvEndereco;
    private Button btnCancelar;

    private GoogleMap mMap;
    private Marker currentLocationMaker;
    private Marker userLocationMaker;
    private LatLng currentLocationLatLong;

    private DatabaseReference refUser;
    private DatabaseReference refPedido;
    private ValueEventListener eventListener;
    private Usuario usuarioAcompanhado;
    private Usuario usuarioLogado;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_acomp);

        tvData = (TextView) findViewById(R.id.tv_data);
        tvDescricao = (TextView) findViewById(R.id.tv_descricao);
        tvDistancia = (TextView) findViewById(R.id.tv_distancia);
        tvEndereco = (TextView) findViewById(R.id.tv_endereco);
        btnCancelar = (Button) findViewById(R.id.btn_cancelar_pedido_agente);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelaPedido(v);
            }
        });
        usuarioLogado = (Usuario) getIntent().getSerializableExtra("usuario");

        carregaUsuario();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        startGettingLocations();
        verificaPedido();
    }

    private void verificaPedido(){
        refPedido = ConfiguracaoFirebase.getFirebaseDatabase().child("pedidos").child(pedidoAtual.getId());
        eventListener = refPedido.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    pedidoAtual = dataSnapshot.getValue(PedidoAcompanhamento.class);

                    if(pedidoAtual.isConcluido()){
                        usuarioLogado.addAcomp();
                        usuarioLogado.salvar();
                        Toast.makeText(AgenteAcompActivity.this, "Parabéns! Você concluiu o atendimento!" ,Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else{
                    finish();
                    Toast.makeText(AgenteAcompActivity.this, usuarioAcompanhado.getNome() + " cancelou o acompanhamento!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cancelaPedido(View view){
        pedidoAtual.setAtivo(false);
        pedidoAtual.salvar();
        finish();
    }

    public void carregaUsuario(){
        refUser = ConfiguracaoFirebase.getFirebaseDatabase();
        String caminho = "usuarios/"+pedidoAtual.getUsuario();
        refUser = refUser.child(caminho);
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    usuarioAcompanhado = dataSnapshot.getValue(Usuario.class);
                    LatLng latLng = new LatLng(usuarioAcompanhado.getLatitude(), usuarioAcompanhado.getLongitude());
                    addGreenMarker(latLng);

                    DecimalFormat df = new DecimalFormat("###,##0.00");
                    double distancia = (float) distanceBetween(usuarioLogado.getLatitude(),usuarioLogado.getLongitude(),
                            usuarioAcompanhado.getLatitude(),usuarioAcompanhado.getLongitude());

                    tvDescricao.setText(usuarioAcompanhado.getNome()+ " (" +
                            usuarioAcompanhado.getTipoPCD() +") precisa de ajuda");
                    tvDistancia.setText(df.format(distancia) + " metros");

                    tvEndereco.setText(pedidoAtual.getLocalizacao());
                    tvData.setText(pedidoAtual.getData());

                    criaDialog();

                    System.out.println("Distância "+distancia);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getMarkers(){
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    usuarioAcompanhado = dataSnapshot.getValue(Usuario.class);
                    LatLng latLng = new LatLng(usuarioAcompanhado.getLatitude(), usuarioAcompanhado.getLongitude());
                    addGreenMarker(latLng);

                    DecimalFormat df = new DecimalFormat("###,##0.00");
                    float distancia = (float) distanceBetween(usuarioLogado.getLatitude(),usuarioLogado.getLongitude(),
                            usuarioAcompanhado.getLatitude(),usuarioAcompanhado.getLongitude());
                    tvDescricao.setText(usuarioAcompanhado.getNome()+ " (" +
                            usuarioAcompanhado.getTipoPCD() +") precisa de ajuda");

                    tvDistancia.setText(df.format(distancia) + " metros");

                    usuarioLogado.salvar();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void criaDialog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Pedido aceito");
        builder.setMessage("Dirija-se até "+ usuarioAcompanhado.getNome()+ " para iniciar o acompanhamento");
        builder.setCancelable(false);
        builder.setNeutralButton("Ok",null);
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEUTRAL).setGravity(Gravity.CENTER_HORIZONTAL);

        Button btnNeutral= alert.getButton(android.app.AlertDialog.BUTTON_NEUTRAL);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnNeutral.getLayoutParams();
        layoutParams.weight = 100;
        layoutParams.gravity = Gravity.CENTER;
        btnNeutral.setLayoutParams(layoutParams);
        btnNeutral.setBackground(getResources().getDrawable(R.drawable.selector_button));
        btnNeutral.setTextColor(Color.WHITE);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // LatLng recife = new LatLng(-8.065638, -34.891130);
        //mMap.addMarker(new MarkerOptions().position(recife).title("Marcador em Recife"));

        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onLocationChanged(Location location) {

        if (currentLocationMaker != null) {
            currentLocationMaker.remove();
        }
        //Add marker
        currentLocationLatLong = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocationLatLong);
        markerOptions.title("Minha Localização");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMaker = mMap.addMarker(markerOptions);

        //Move to new location
        //CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(currentLocationLatLong).build();
        // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Toast.makeText(this, "Localização atualizada", Toast.LENGTH_SHORT).show();
        usuarioLogado.setLatitude(location.getLatitude());
        usuarioLogado.setLongitude(location.getLongitude());
        getMarkers();

    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS desativado!");
        alertDialog.setMessage("Ativar GPS?");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    private void startGettingLocations() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean canGetLocation = true;
        int ALL_PERMISSIONS_RESULT = 101;
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// Distance in meters
        long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        //Check if GPS and Network are on, if not asks the user to turn on
        if (!isGPS && !isNetwork) {
            showSettingsAlert();
        } else {
            // check permissions

            // check permissions for later versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    canGetLocation = false;
                }
            }
        }


        //Checks if FINE LOCATION and COARSE Location were granted
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
            return;
        }

        //Starts requesting location updates
        if (canGetLocation) {
            if (isGPS) {
                lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            } else if (isNetwork) {
                // from Network Provider

                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            }
        } else {
            Toast.makeText(this, "Não é possível obter a localização", Toast.LENGTH_SHORT).show();
        }
    }


    private void addGreenMarker( LatLng latLng) {
        if (userLocationMaker != null) {
            userLocationMaker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Localização da solicitação");
        markerOptions.snippet(usuarioAcompanhado.getNome() + ": Deficiência: "+ usuarioAcompanhado.getTipoPCD());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        userLocationMaker = mMap.addMarker(markerOptions);
        //Move to new location
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(latLng).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private float distanceBetween(double lat1, double lng1, double lat2, double lng2) {
        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);

        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);

        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        return loc1.distanceTo(loc2);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}


