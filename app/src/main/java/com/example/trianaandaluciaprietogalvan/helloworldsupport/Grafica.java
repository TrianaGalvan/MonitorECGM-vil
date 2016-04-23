package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.message.GraficarValorEvent;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.service.ServiceECG;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Grafica extends AppCompatActivity {

    Intent intent;

    @Bind(R.id.senal_cardiaca)
    LineChart grafica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        //Creacion y configuracion de la gráfica.
        grafica.setData(new LineData());
        /*grafica.getAxisRight().setAxisMaxValue(5000f);
        grafica.getAxisRight().setAxisMinValue(0f);
        grafica.getAxisRight().setShowOnlyMinMax(false);
        grafica.getAxisLeft().setAxisMaxValue(5000f);
        grafica.getAxisLeft().setAxisMinValue(0f);
        grafica.getAxisLeft().setShowOnlyMinMax(false);*/
        grafica.setScaleMinima(1.6f,1f);
        grafica.setHardwareAccelerationEnabled(true);

        grafica.invalidate();

        /*LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("ECG"));*/
        startService();

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
    public void onEventoGraficar(GraficarValorEvent event){
        LineData data = grafica.getData();

        int val = event.numero;

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

    /*BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    LineData data = grafica.getData();

                    int val = intent.getIntExtra("val", 0);
                    if(data != null)
                    {
                        ILineDataSet set = data.getDataSetByIndex(0);
                        if(set == null)
                        {
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
                        grafica.moveViewTo(data.getXValCount()-7, 50f, YAxis.AxisDependency.LEFT);
                    }
                }
            });

        }
    };*/



    // Method to start the service
    public void startService() {
        intent = new Intent(getBaseContext(), ServiceECG.class);
        startService(intent);
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(intent);
        Toast.makeText(this,"Se detuvo el wervicio",Toast.LENGTH_SHORT).show();
    }


}
