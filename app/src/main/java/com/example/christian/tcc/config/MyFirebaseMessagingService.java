package com.example.christian.tcc.config;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;

import com.example.christian.tcc.R;
import com.example.christian.tcc.activitys.AgenteAcompActivity;
import com.example.christian.tcc.activitys.AgenteMainActivity;
import com.example.christian.tcc.activitys.DialogActivity;
import com.example.christian.tcc.modelo.PedidoAcompanhamento;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.christian.tcc.config.ChecaSegundoPlano.isActivityVisible;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static PedidoAcompanhamento pedidoAtual;
    public static Map<String, String> dataMap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.

        if(remoteMessage.getData() !=null) {

            dataMap = remoteMessage.getData();

            pedidoAtual = new PedidoAcompanhamento();
            pedidoAtual.setId( dataMap.get("id").toString() );
            pedidoAtual.setUsuario( dataMap.get("usuario").toString() );

            if(isActivityVisible()) {
                startActivity(new Intent(this, DialogActivity.class));
            }

            else
                sendNotificication();
        }


    }

    public void sendNotificication(){
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /* BUTTON - Aceitar*/
        Intent aceitar = new Intent(this, AgenteAcompActivity.class);
        aceitar.setAction( "action-aceitar" );
        PendingIntent aceitarPI = PendingIntent.getActivity(
                this,
                0,
                aceitar,
                0);

        NotificationCompat.Action aceitarAc = new NotificationCompat.Action(
                R.drawable.ic_thumb_up,
                "Aceitar",
                aceitarPI);

        /* BUTTON - Recursar */
        Intent recusar = new Intent(this, AgenteMainActivity.class);
        recusar.setAction( "action-recusar" );
        PendingIntent recusarPI = PendingIntent.getActivity(
                this,
                0,
                recusar,
                0);

        NotificationCompat.Action recusarAc = new NotificationCompat.Action(
                R.drawable.ic_thumb_down,
                "Recusar",
                recusarPI);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_accessible);
        builder.setContentTitle(dataMap.get("titulo"));
        builder.setContentText(dataMap.get("descricao"));
        builder.addAction(aceitarAc);
        builder.addAction(recusarAc);
        // builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.vibrate = new long[]{150, 300, 150, 600};
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.ic_accessible, notification);

        try {

            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("não está rodando");

    }

    private boolean verifyApplicationRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < procInfos.size(); i++) {
            if (procInfos.get(i).processName.equals("com.example.christian.tcc")) {
                onDestroy();
                return true;
            }
        }
        return false;
    }


}


