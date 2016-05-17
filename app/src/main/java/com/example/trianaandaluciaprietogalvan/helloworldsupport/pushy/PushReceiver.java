package com.example.trianaandaluciaprietogalvan.helloworldsupport.pushy;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.R;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.VerDetallesPrueba;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;

public class PushReceiver extends BroadcastReceiver
{

    String WHERE_REPORTE = MonitorECGContrato.ReporteEntry._ID + " = ?";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent)
    {

        String recomendaciones = intent.getStringExtra("recomendaciones");
        String id = intent.getStringExtra("idReporte");
        String status = intent.getStringExtra("status");
        String fecha = intent.getStringExtra("fecha");
        String idPrueba = intent.getStringExtra("idPrueba");

        //actualizar el estado del reporte
        ContentResolver cr = context.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(MonitorECGContrato.ReporteEntry.COLUMN_ESTATUS,status);
        cv.put(MonitorECGContrato.ReporteEntry.COLUMN_RECOMENDACIONES, recomendaciones);

        String[] valores = new String[]{
                id
        };

        int rowsu = cr.update(MonitorECGContrato.ReporteEntry.CONTENT_URI, cv, WHERE_REPORTE, valores);

        if(rowsu != 0){
            Log.d("PushReceiver", "Pushy: recomendaciones:" + recomendaciones + "id:" + id + "status:" + status);

            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.icono180)
                            .setContentTitle("Recomendación actualizada")
                            .setContentText("Monitor ECG    Prueba: " + fecha)
                            .setAutoCancel(true);

            Uri uriPrueba = MonitorECGContrato.PruebaEntry.buildPruebaId(Integer.parseInt(idPrueba));

            Intent intentDetalle = new Intent(context, VerDetallesPrueba.class);
            intentDetalle.setData(uriPrueba);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            intentDetalle,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);

            // Sets an ID for the notification
            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId,mBuilder.build());

        }

    }

    /*public void lanzarNotificacion(String tittle, String content,Context context){
        // Create a test notification
        Notification notification = new Notification(R.drawable.icono180, content, System.currentTimeMillis());

        // Sound + vibrate + light
        notification.defaults = Notification.DEFAULT_ALL;

        // Dismisses when pressed
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        // Set notification click intent
        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, Historial.class), 0);

        // Set title and desc
        notification.setLatestEventInfo(context, tittle, content, notificationIntent );

        // Get notification manager
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Dispatch the notification
        mNotificationManager.notify(0, notification);
    }*/

}