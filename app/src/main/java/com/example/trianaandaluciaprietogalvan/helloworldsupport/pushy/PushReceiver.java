package com.example.trianaandaluciaprietogalvan.helloworldsupport.pushy;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.Historial;

public class PushReceiver extends BroadcastReceiver
{

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent)
    {

        String recomendaciones = intent.getStringExtra("recomendaciones");
        String id = intent.getStringExtra("idReporte");
        String status = intent.getStringExtra("status");



        Log.d("PushReceiver", "Pushy: recomendaciones:" + recomendaciones + "id:" + id + "status:" + status);


        Notification notification = new Notification.Builder(context)
                .setContentTitle("Actualización de las recomendaciones")
                .setContentText("Monitor ECG")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();


        // Create a test notification
        /*Notification notification = new Notification(android.R.drawable.ic_dialog_info, notificationDesc, System.currentTimeMillis());

        // Sound + vibrate + light
        notification.defaults = Notification.DEFAULT_ALL;

        // Dismisses when pressed
        notification.flags = Notification.FLAG_AUTO_CANCEL;*/

        // Set notification click intent
        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, Historial.class), 0);

        // Set title and desc
        //notification.setLatestEventInfo(context, notificationTitle, notificationDesc, notificationIntent );
        notification.contentIntent = notificationIntent;

        // Get notification manager
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Dispatch the notification
        mNotificationManager.notify(0, notification);
    }

}