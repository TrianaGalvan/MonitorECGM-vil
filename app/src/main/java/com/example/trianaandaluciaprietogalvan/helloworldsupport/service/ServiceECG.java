package com.example.trianaandaluciaprietogalvan.helloworldsupport.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;

import java.util.ArrayList;

/**
 * Created by trianaandaluciaprietogalvan on 19/04/16.
 */
public class ServiceECG extends Service {

    public static final String CONSTAT_ACTUALIZAR_UI = "MyServiceUpdate";

    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    private long startTime = 0;
    private long millis = 0;
    ArrayList<Integer> datos = new ArrayList<>();
    ObtenerDatos obtenerDatos;
    EnviarDatos enviarDatos;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        obtenerDatos = (ObtenerDatos) new ObtenerDatos().execute();
        enviarDatos = (EnviarDatos) new EnviarDatos().execute();

        //Do what you need in onStartCommand when service has been started
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }

    public class ObtenerDatos extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for(int i = 0;i < 100 ; i++){
                datos.add(i);
            }
            return null;
        }
    }


    public class EnviarDatos extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while(true)
            {
                if(isCancelled())
                    break;
                try
                {
                    Thread.sleep(33);
                    publishProgress();
                }
                catch(InterruptedException ie)
                {

                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            for(int i = 0;i < 100 ; i++){
                datos.get(i);
                System.out.println("Datos: " + i);
                Intent intent = new Intent("speedExceeded");
                intent.putExtra("num",i);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
            }
        }
    }

    @Override
    public void onDestroy() {
        enviarDatos.cancel(true);
        super.onDestroy();
    }
}

