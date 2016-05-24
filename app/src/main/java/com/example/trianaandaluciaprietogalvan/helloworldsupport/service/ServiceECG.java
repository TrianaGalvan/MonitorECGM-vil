package com.example.trianaandaluciaprietogalvan.helloworldsupport.service;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.CapturarMessage;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.ColocarFrecuenciaEvent;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.Comando1Message;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.ErroEnlazadoECG;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.GraficarValorEvent;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.ProgressDialogGraficaEvent;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.ServiceECGErrorsEvent;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.FileUtilPrueba;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

/**
 * Created by trianaandaluciaprietogalvan on 19/04/16.
 */
public class ServiceECG extends Service {

    public static final String PARAM_NAME_FILE = "nombreArchivo";
    public static final String TIPO_HILO = "tipo-hilo";
    Context context = getBaseContext();

    //PROYECCION PARA OBTENER EL DISPOSITIVO
    String[] PROYECCION_DISPOSITIVO = new String[]{
            MonitorECGContrato.DispositivoEntry.COLUMN_NOMBRE
    };

    //COLUMNAS DE LA PROYECCION
    public static final int COLUMN_NOMBRE_DISP = 0;

    static final UUID miUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final String CONSTAT_ACTUALIZAR_UI = "MyServiceUpdate";
    ProgressDialog progreso;
    BluetoothSocket btSocket=null;
    BluetoothAdapter bluetooth;

    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    private long startTime = 0;
    private long millis = 0;
    IntercambioDatos intercambio;
    LeerArchivo leerArchivo;
    ConexionBT conexion;
    boolean estadoBt;
    boolean estadoConexion;
    boolean banderaCapturar = false;
    int tipoVal;
    //archivo de la prueba
    File pruebaFile;
    OutputStreamWriter fileOutput;
    InputStream inputStream;

    String ar;

    int indiceInicio = 0, indiceFinal = 0;

    @Subscribe
    public void onCapturarPrueba(CapturarMessage event){
        fileOutput = FileUtilPrueba.generarArchivio(ar);
        banderaCapturar = event.bandera;
        EventBus.getDefault().post(new ServiceECGErrorsEvent("Capturando muestra"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Registrar el event bus
        EventBus.getDefault().register(this);

        Bundle bundle = intent.getExtras();
        ar  = bundle.getString(PARAM_NAME_FILE);

        String tipoHilo = "";
        tipoHilo = bundle.getString(TIPO_HILO);

        if(tipoHilo.equals("Ver")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                leerArchivo = (LeerArchivo) new LeerArchivo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                leerArchivo = (LeerArchivo) new LeerArchivo().execute();
            }
        }
        else{
            conexion = (ConexionBT) new ConexionBT().execute();
        }

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
                EventBus.getDefault().post(new Comando1Message("Error al obtener los streams"));
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            leerBluetooth();
            return null;
        }

        public void leerBluetooth() {
            int n1, n2;
            int nBytes;
            // Read from the InputStream
            try {
                os.write(1);
            } catch (IOException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new Comando1Message("No se envió el comando 1"));
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
                    if (aux3 == 128) {
                        if (aux1 == 0) {
                            aux1 = n1 & 0x0F;
                            aux2 = n2 & 0x0F;
                            aux2 = aux2 << 4;
                            aux1 = aux1 | aux2;
                        } else {
                            aux1 = n1 & 0x0F;
                            aux2 = n2 & 0x0F;
                            aux1 = aux1 << 4;
                            aux1 = aux1 | aux2;
                        }
                        Thread.sleep(5);
                        float frecuencia = (256.0f / (float) aux1) * 70.0f;
                        if (Float.isInfinite(frecuencia) || Float.isNaN(frecuencia))
                            frecuencia = 0;
                        EventBus.getDefault().post(new ColocarFrecuenciaEvent(frecuencia));
                    } else {
                        if (aux1 == 0) {
                            aux1 = n1 & 0x3F;
                            aux2 = n2 & 0x3F;
                            aux2 = aux2 << 6;
                            aux1 = aux1 | aux2;
                        } else {
                            aux1 = n1 & 0x3F;
                            aux2 = n2 & 0x3F;
                            aux1 = aux1 << 6;
                            aux1 = aux1 | aux2;
                        }
                        Thread.sleep(5);
                        publishProgress(aux1);
                        if(banderaCapturar){
                            fileOutput.write(Integer.toString(aux1) + "\n");
                        }
                    }
                } catch (IOException e) {
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void leerArchivo() {
            Integer[] valores = new Integer[2];
            int aux1, aux2, aux3, aux4;
            int n1, n2;
            InputStream inputStream = null;
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String files = file.getAbsolutePath() + "/muestra_300hz_16bits.txt";
            String ruta = "/storage/emulated/0/Download/muestra_300hz_16bits.txt";

            try {
                inputStream = new FileInputStream(new File(files));
                if(inputStream != null){
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    String val2 = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((receiveString = bufferedReader.readLine()) != null){
                        val2 = bufferedReader.readLine();
                        if (isCancelled())
                            break;
                        n1 = Integer.parseInt(receiveString);
                        n2 = Integer.parseInt(val2);
                        aux1 = n1 & 64;
                        aux3 = n1 & 128;

                        if (aux3 == 128) {
                            if (aux1 == 0) {
                                aux1 = n1 & 0x0F;
                                aux2 = n2 & 0x0F;
                                aux2 = aux2 << 4;
                                aux1 = aux1 | aux2;
                            } else {
                                aux1 = n1 & 0x0F;
                                aux2 = n2 & 0x0F;
                                aux1 = aux1 << 4;
                                aux1 = aux1 | aux2;
                            }
                            Thread.sleep(5);
                            float frecuencia = (256.0f / (float) aux1) * 70.0f;
                            if (Float.isInfinite(frecuencia) || Float.isNaN(frecuencia))
                                frecuencia = 0;
                            EventBus.getDefault().post(new ColocarFrecuenciaEvent(frecuencia));
                        } else {
                            if (aux1 == 0) {
                                aux1 = n1 & 0x3F;
                                aux2 = n2 & 0x3F;
                                aux2 = aux2 << 6;
                                aux1 = aux1 | aux2;
                            } else {
                                aux1 = n1 & 0x3F;
                                aux2 = n2 & 0x3F;
                                aux1 = aux1 << 6;
                                aux1 = aux1 | aux2;
                            }
                            Thread.sleep(5);
                            publishProgress(aux1);
                            if(banderaCapturar){
                                fileOutput.write(Integer.toString(aux1) + "\n");
                                Log.i("Service ECG","------------- GENERANDO ARCHIVO ---------------");
                            }
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
            indiceInicio = 0;
            try {
                if(is != null && os != null){
                    is.close();
                    os.close();
                }else if(btSocket != null){
                    btSocket.close();
                    btSocket = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConexionBT extends AsyncTask<Bundle, Void, Void> {

        public ConexionBT() {
            estadoConexion = false;
        }

        @Override
        protected void onPreExecute() {
            EventBus.getDefault().post(new ProgressDialogGraficaEvent("Conectando...","Por favor espere..."));
        }

        @Override
        protected Void doInBackground(Bundle... dispositivos) {
            try {
                if (btSocket == null || !estadoBt) {
                    bluetooth = BluetoothAdapter.getDefaultAdapter();
                    //obtener el dispositivo a enlazar
                    ContentResolver rs = getContentResolver();
                    Cursor cursor = rs.query(MonitorECGContrato.DispositivoEntry.CONTENT_URI, PROYECCION_DISPOSITIVO, null, null, null);
                    if (cursor != null) {
                        if(cursor.getCount() != 0){
                            cursor.moveToFirst();
                            String nombreDispositivo = cursor.getString(COLUMN_NOMBRE_DISP);
                            String direccion = nombreDispositivo.substring((nombreDispositivo.length() - 17));
                            BluetoothDevice dispositivo = bluetooth.getRemoteDevice(direccion);
                            btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(miUUID);
                            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                            btSocket.connect();
                        }else {
                            EventBus.getDefault().post(new ErroEnlazadoECG("El dispositivo no se encuentra enlazado con algún monitor ecg"));
                        }
                    }
                }
            } catch (IOException ioe) {
                estadoConexion = false;
            }
            estadoConexion = true;
            return  null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Progress dismiss
            EventBus.getDefault().post(new ProgressDialogGraficaEvent("", ""));
            if (!estadoConexion) {
                EventBus.getDefault().post(new Comando1Message("Error de conexión, vuelva a intentarlo"));
            } else {
                EventBus.getDefault().post(new ServiceECGErrorsEvent("Conexion establecida"));
                estadoBt = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    intercambio = (IntercambioDatos) new IntercambioDatos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    intercambio = (IntercambioDatos) new IntercambioDatos().execute();
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            try {
                if(btSocket != null){
                    btSocket.close();
                    btSocket = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class LeerArchivo extends AsyncTask<Void, Integer, Void> {
        InputStream inputStreamLeer;
        public LeerArchivo(){
            inputStreamLeer = null;
        }

        @Override
        protected void onPreExecute() {
            File pruebaFile = new File(Environment.getExternalStorageDirectory(),ar);
            try {
                inputStreamLeer = new FileInputStream(pruebaFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            leerArchivo();
            return null;
        }

        public void leerArchivo() {
            int aux1, aux2, aux3, aux4;
            int n1, n2;
            try {
                if(inputStreamLeer != null){
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStreamLeer);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    String val2 = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((receiveString = bufferedReader.readLine()) != null){
                        if (isCancelled())
                            break;
                        n1 = Integer.parseInt(receiveString);
                        Thread.sleep(5);
                        publishProgress(n1);
                    }

                    inputStreamLeer.close();
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
            try {
                inputStreamLeer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        if(intercambio != null){
            intercambio.cancel(true);
            intercambio = null;
            banderaCapturar = false;
            try {
                if(fileOutput != null){
                    fileOutput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(conexion != null){
            try{
                conexion.cancel(true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(leerArchivo != null){
            leerArchivo.cancel(true);
            leerArchivo = null;
        }
        inputStream = null;
    }
}

