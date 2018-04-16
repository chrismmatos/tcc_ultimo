package com.example.christian.tcc.config;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.christian.tcc.R;
import com.example.christian.tcc.activitys.AgenteAcompActivity;
import com.example.christian.tcc.activitys.AgenteMainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.

        if(remoteMessage.getData() !=null) {

            Map<String, String> dataMap = remoteMessage.getData();
            String usuario = dataMap.get("usuario").toString();
            String pedido = dataMap.get("pedido").toString();
            System.out.println(usuario + " " + pedido);

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            /* BUTTON - OK, ENTENDI */
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

            /* BUTTON - ENTRAR */
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

//            PendingIntent pendingIntent = PendingIntent.getActivity(
//                    this, 0, new Intent(this, AgenteAcompActivity.class), 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.ic_accessible);
            builder.setContentTitle(dataMap.get("titulo"));
            builder.setContentText(dataMap.get("descricao"));
            builder.addAction( aceitarAc );
            builder.addAction( recusarAc );
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
        }


    }


    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, .class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
//                        .setContentTitle("FCM Message")
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}


