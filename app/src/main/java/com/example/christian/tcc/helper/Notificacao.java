package com.example.christian.tcc.helper;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;

import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.example.christian.tcc.modelo.PedidoAcompanhamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.christian.tcc.activitys.LoginAct.usuarioLogado;
import static com.example.christian.tcc.config.MyFirebaseMessagingService.pedidoAtual;

public class Notificacao {

    public static final String SERVER_KEY = "AAAA7skqMcQ:APA91bFNM_stckhqzLOVd6xDTJ1jCvRHJ2oTPrl0W0GXTWswTx5uBNSRjTKyFUu9UKy0Hb6wZGcw1i7lA9CvQVvVNkqJ50QU6qMNOX0iXMu0P6Jf7cmOPteQwmHBqcxOv3eugJ8Nj_eh";

    public static final String SENDER = "cSRp6zqNPn4:APA91bHz4HUPrFkLDqxH32oH58p8fSpKaGZntNvg5K5wFGTqK9VGD1QwQmSKm8P2dW4HgoG0Ndj1mME6LoDulaFQZVrGPTuSMkC8rj4lqBwE4BtP_20BbFUE4eYBfQG0jy_pq6Lsubod";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static boolean aceito = false ;

    public static void sendNotification(final String regToken, final JSONObject dataNotification) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json  =new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    JSONObject ttl = new JSONObject();
                    ttl.put("ttl","60s");
                    dataJson.put("body","Hi this is sent from device to device");
                    dataJson.put("title","dummy title");
                    //json.put("notification",dataJson);
                    json.put("to",regToken);
                    json.put("priority","high");
                    json.put("data", dataNotification);
                    json.put ("android",ttl);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key="+SERVER_KEY)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    System.out.println("Resposta http"+finalResponse);
                }catch (Exception e){
                    //Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();

    }

    public static String retornaHora(){
        String hora;
        Date dataHoraAtual = new Date();
        hora =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataHoraAtual);

        System.out.println ("hora atual " + hora);

        return hora;
    }

    public static boolean isAceito(final Context context){

        NotificationManager notifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notifyManager.cancelAll();
        DatabaseReference refPedido = ConfiguracaoFirebase.getFirebaseDatabase();
        String caminho = "pedidos/"+pedidoAtual.getId();
        refPedido = refPedido.child(caminho);

        refPedido.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    pedidoAtual = dataSnapshot.getValue(PedidoAcompanhamento.class);
                    if (!pedidoAtual.isAtivo()) {
                        pedidoAtual.setAcompanhante(usuarioLogado.getId());
                        pedidoAtual.setAtivo(true);
                        pedidoAtual.salvar();
                        aceito = false;
                    }
                    else
                        aceito = true;
                }

                else {
                    aceito = true;
                    System.out.println("pedido nao está ativo");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        return aceito;
    }

}
