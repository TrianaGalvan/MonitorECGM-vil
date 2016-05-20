package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.CapturarMessage;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.ColocarFrecuenciaEvent;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.GraficarValorEvent;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.ProgressDialogGraficaEvent;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.ServiceECGErrorsEvent;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.service.ServiceECG;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.FileUtilPrueba;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Grafica extends AppCompatActivity {

    public static final String PARAM_SERVICE = "initServicio";
    public static final String PARAM_FRECUENCIA = "frecuencia";
    public static final String PARAM_NAME_FILE = "nombreArchivo";
    Intent intent;

    String NOMBRE_ARCHIVO_PRUEBA = "";

    @Bind(R.id.senal_cardiaca)
    LineChart grafica;
    @Bind(R.id.buttonDetener)
    Button detener;
    @Bind(R.id.buttonEmpezar)
    Button empezar;
    @Bind(R.id.buttonCapturar)
    Button capturar;
    @Bind(R.id.txtFrecuencia)
    TextView frecuencia;

    String valParamService = "";
    String frecuenciaCardiaca = "";
    ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        //Creacion y configuracion de la gráfica.
        grafica.setData(new LineData());
        grafica.setScaleMinima(1.6f, 1f);
        grafica.setHardwareAccelerationEnabled(true);
        grafica.invalidate();
        frecuencia.setText("0 Ipm");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            valParamService = bundle.getString(PARAM_SERVICE);
            //verificar los botones
            assert valParamService != null;
            if(valParamService.equals("ver")){
                frecuenciaCardiaca = bundle.getString(PARAM_FRECUENCIA);
                NOMBRE_ARCHIVO_PRUEBA = bundle.getString(PARAM_NAME_FILE);
                frecuencia.setText(frecuenciaCardiaca+" Ipm");
                empezar.setVisibility(View.INVISIBLE);
                capturar.setVisibility(View.INVISIBLE);
                detener.setText("Salir");
                startService("Ver");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEventoGraficar(GraficarValorEvent event) {
        LineData data = grafica.getData();
        int val = event.valor;
        if(data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                LineDataSet setaux = new LineDataSet(null, "DataSet 1");
                setaux.setLineWidth(2.5f);
                setaux.setCircleRadius(0f);
                setaux.setColor(Color.rgb(240, 99, 99));
                setaux.setCircleColor(Color.rgb(240, 99, 99));
                setaux.setHighLightColor(Color.rgb(190, 190, 190));
                setaux.setAxisDependency(YAxis.AxisDependency.LEFT);
                setaux.setDrawCubic(true);
                set = setaux;
                data.addDataSet(set);
            }
            data.addXValue(set.getEntryCount() + "");
            int random = (int) (Math.random() * data.getDataSetCount());
            data.addEntry(new Entry(val, set.getEntryCount()), random);
            grafica.notifyDataSetChanged();
            grafica.moveViewTo(data.getXValCount() - 7, 50f, YAxis.AxisDependency.LEFT);

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startProgressDialog(ProgressDialogGraficaEvent event){
        if(event.message.isEmpty()){
            progreso.dismiss();
        }else{
            progreso = new ProgressDialog(Grafica.this);
            progreso.setMessage(event.message);
            progreso.setTitle(event.messageTittle);
            progreso.show();
        }
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void mostrarMsjError(ServiceECGErrorsEvent event){
        Toast.makeText(getBaseContext(), event.error, Toast.LENGTH_LONG).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onColocarFrecuencia(ColocarFrecuenciaEvent event){
        float f = event.frecuencia;
        String frec = String.format("%.2f",f);
        frecuencia.setText(frec + " Ipm");
    }

    // Method to start the service
    public void startService(String tipoBoton) {
        if(tipoBoton.equals("Empezar")){
            NOMBRE_ARCHIVO_PRUEBA = FileUtilPrueba.generarNombreArch(getBaseContext());
        }
        //Crear el arcivo donde se va  a guardar la prueba
        intent = new Intent(getBaseContext(), ServiceECG.class);
        Bundle bundle = new Bundle();
        //obtener la conexión con el bluethoot
        bundle.putString(ServiceECG.PARAM_NAME_FILE,NOMBRE_ARCHIVO_PRUEBA);
        bundle.putString(ServiceECG.TIPO_HILO,tipoBoton);
        intent.putExtras(bundle);
        startService(intent);
    }


    // Method to stop the service
    public void stopService(View view) {
        if(detener.getText().toString().equals("Salir")){
            stopService(intent);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }else{
            showDialog();
        }
    }

    public void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Salir");

        // set dialog message
        alertDialogBuilder
                .setMessage("¿Estas seguro de detener el electrocardiograma?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        empezar.setEnabled(true);
                        stopService(intent);
                        Intent intent = new Intent(getBaseContext(), EnviarECG.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(EnviarECG.PARAM_FREC,frecuencia.getText().toString());
                        bundle.putString(EnviarECG.PARAM_NOMBRE_ARCHIVO, NOMBRE_ARCHIVO_PRUEBA);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }


    public void startService(View view) {
        empezar.setVisibility(View.INVISIBLE);
        startService("Empezar");
    }

    public void startServiceCapturar(View view) {
        EventBus.getDefault().post(new CapturarMessage(true));
    }
}
