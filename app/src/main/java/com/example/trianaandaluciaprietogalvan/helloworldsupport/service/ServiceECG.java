package com.example.trianaandaluciaprietogalvan.helloworldsupport.service;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.ColocarFrecuenciaEvent;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.GraficarValorEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by trianaandaluciaprietogalvan on 19/04/16.
 */
public class ServiceECG extends Service {

    //PROYECCION PARA OBTENER EL DISPOSITIVO
    String[] PROYECCION_DISPOSITIVO = new String[]{
            MonitorECGContrato.DispositivoEntry.COLUMN_NOMBRE
    };

    //COLUMNAS DE LA PROYECCION
    public static final int COLUMN_NOMBRE_DISP = 0;

    static final UUID miUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final String CONSTAT_ACTUALIZAR_UI = "MyServiceUpdate";
    ProgressDialog progreso;
    BluetoothSocket btSocket;
    BluetoothAdapter bluetooth;

    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    private long startTime = 0;
    private long millis = 0;
    IntercambioDatos intercambio;
    ConexionBT conexion;
    boolean estadoBt;
    boolean estadoConexion;
    int tipoVal;


    int indiceInicio = 0, indiceFinal = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //iniciar la conexion con el bluethoot

        //DESCOMENTAR PARA PROBAR CON BLUETOOTH
        /*conexion = (ConexionBT) new ConexionBT().execute();
        //esperar a que se inicie la conexion con el bluethoot
        while (true) {
            if (estadoConexion) {
                break;
            }
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            intercambio = (IntercambioDatos) new IntercambioDatos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        } else {
            intercambio = (IntercambioDatos) new IntercambioDatos().execute();
        }


        //Do what you need in onStartCommand when service has been started
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class IntercambioDatos extends AsyncTask<Void, Integer, Void> {

        InputStream is;
        OutputStream os;
        boolean estadoConexion;

        public IntercambioDatos() {
            is = null;
            os = null;
            estadoConexion = true;
        }


        @Override
        protected void onPreExecute() {
            //DESCOMENTAR PARA USAR BLUETOOTH
           try {
                is = btSocket.getInputStream();
                os = btSocket.getOutputStream();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                //Toast.makeText(ServiceECG.this, "Error al obtener los streams", Toast.LENGTH_LONG).show();
            }*/
        }

        @Override
        protected Void doInBackground(Void... params) {
            leerArchivo();
            return null;
        }

        public void leerBluetooth(){

            Integer[] valores = new Integer[2];
            int n1, n2;
            int nBytes;
            // Read from the InputStream
            try {
                os.write(1);
            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(ServiceECG.this, "No se envio el comando 1", Toast.LENGTH_LONG).show();
            }
            while (true) {
                if (isCancelled())
                    break;
                try {
                    int aux1, aux2, aux3;
                    n1 = is.read();
                    n2 = is.read();

                    aux1 = n1 & 64;
                    aux3 = n1 & 128;
                    if(aux3 == 128){
                        if(aux1 == 0)
                        {
                            aux1 = n1 & 0x0F;
                            aux2 = n2 & 0x0F;
                            aux2 = aux2 << 4;
                            aux1 = aux1 | aux2;
                        }
                        else
                        {
                            aux1 = n1 & 0x0F;
                            aux2 = n2 & 0x0F;
                            aux1 = aux1 << 4;
                            aux1 = aux1 | aux2;
                        }
                        Thread.sleep(5);
                        float frecuencia = (256.0f / (float)aux1) * 70.0f;
                        EventBus.getDefault().post(new ColocarFrecuenciaEvent(frecuencia));
                    }else
                    {
                        if(aux1 == 0)
                        {
                            aux1 = n1 & 0x3F;
                            aux2 = n2 & 0x3F;
                            aux2 = aux2 << 6;
                            aux1 = aux1 | aux2;
                        }
                        else
                        {
                            aux1 = n1 & 0x3F;
                            aux2 = n2 & 0x3F;
                            aux1 = aux1 << 6;
                            aux1 = aux1 | aux2;
                        }
                        Thread.sleep(5);
                        publishProgress(aux1);
                    }
                } catch (IOException e) {
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void leerArchivo(){
            Integer[] valores = new Integer[2];
            int aux1, aux2, aux3, aux4;
            int n1, n2;
            InputStream inputStream = null;
            File file   = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String files = file.getAbsolutePath()+"/muestra_300hz_16bits.txt";
            String ruta = "/storage/emulated/0/Download/muestra_300hz_16bits.txt";
            try {
                inputStream = new FileInputStream(new File(files));
                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        String val2 = bufferedReader.readLine();
                        if (isCancelled())
                            break;
                            n1 = Integer.parseInt(receiveString);
                            n2 = Integer.parseInt(val2);
                            aux1 = n1 & 64;
                            aux3 = n1 & 128;

                            if(aux3 == 128){
                                if(aux1 == 0)
                                {
                                    aux1 = n1 & 0x0F;
                                    aux2 = n2 & 0x0F;
                                    aux2 = aux2 << 4;
                                    aux1 = aux1 | aux2;
                                }
                                else
                                {
                                    aux1 = n1 & 0x0F;
                                    aux2 = n2 & 0x0F;
                                    aux1 = aux1 << 4;
                                    aux1 = aux1 | aux2;
                                }
                                Thread.sleep(5);
                                float frecuencia;
                                if(aux1 != 0){
                                    frecuencia = (256.0f / (float)aux1) * 70.0f;
                                }else{
                                    frecuencia = 0;
                                }
                                EventBus.getDefault().post(new ColocarFrecuenciaEvent(frecuencia));
                            }else
                            {
                                if(aux1 == 0)
                                {
                                    aux1 = n1 & 0x3F;
                                    aux2 = n2 & 0x3F;
                                    aux2 = aux2 << 6;
                                    aux1 = aux1 | aux2;
                                }
                                else
                                {
                                    aux1 = n1 & 0x3F;
                                    aux2 = n2 & 0x3F;
                                    aux1 = aux1 << 6;
                                    aux1 = aux1 | aux2;
                                }
                                Thread.sleep(5);
                                publishProgress(aux1);
                            }

                    }

                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Integer valor = values[0];
            EventBus.getDefault().post(new GraficarValorEvent(valor));
        }

        @Override
        protected void onCancelled(Void aVoid) {
            //USAR PARA EL BLUETHOOT
           /* try {
                os.write(0);
                indiceInicio = 0;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                //Toast.makeText(ServiceECG.this, "No se enio el comando 0", Toast.LENGTH_LONG).show();
            }*/
        }
    }



    private class ConexionBT extends AsyncTask<Void, Void, Void> {

        public ConexionBT() {
            estadoConexion = false;
        }

        @Override
        protected void onPreExecute() {
            //progreso = ProgressDialog.show(ServiceECG.this, "Connectando...", "Por favor espere...");
        }

        @Override
        protected Void doInBackground(Void... dispositivos) {
            try {
                if (btSocket == null || !estadoBt) {
                    bluetooth = BluetoothAdapter.getDefaultAdapter();
                    //obtener el dispositivo a enlazar
                    ContentResolver rs = getContentResolver();

                    Cursor cursor = rs.query(MonitorECGContrato.DispositivoEntry.CONTENT_URI, PROYECCION_DISPOSITIVO, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        String nombreDispositivo = cursor.getString(COLUMN_NOMBRE_DISP);
                        String direccion = nombreDispositivo.substring((nombreDispositivo.length() - 17));
                        BluetoothDevice dispositivo = bluetooth.getRemoteDevice(direccion);
                        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(miUUID);
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        btSocket.connect();
                    } else {
                        //Toast.makeText(getBaseContext(), "El dispositivo no se encuentra enlazado con algún monitor ecg", Toast.LENGTH_LONG).show();
                    }

                }
            } catch (IOException ioe) {
                estadoConexion = false;
            }
            estadoConexion = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!estadoConexion) {
               // Toast.makeText(ServiceECG.this, "Error de conexión, vuelva a intentarlo", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(ServiceECG.this, "Conexion establecida", Toast.LENGTH_LONG).show();
                estadoBt = true;
            }
            //progreso.dismiss();
        }

    }

    @Override
    public void onDestroy() {
        intercambio.cancel(true);
        intercambio = null;
    }
}

