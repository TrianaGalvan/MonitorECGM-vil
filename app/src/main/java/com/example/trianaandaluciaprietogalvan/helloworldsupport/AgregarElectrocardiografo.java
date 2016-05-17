package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AgregarElectrocardiografo extends AppCompatActivity {
    String[] electrocardiografos = null;
    BluetoothAdapter bluetooth;
    Set<BluetoothDevice> dispositivosEnlazados;

    ECGSAdapter ecgsAdapter;

    String[] PROYECCION_DISP = new String[]{
            MonitorECGContrato.DispositivoEntry.COLUMN_NOMBRE
    };

    @Bind(R.id.lista_electrocardiogramas)
    ListView lista;

    public AgregarElectrocardiografo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_agregar_electrocardiografo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ButterKnife.bind(this);
        ecgsAdapter = new ECGSAdapter(this,R.layout.electrocardiograma_row);

        ArrayList<String> nombresispositivos = enlazar();
        if(nombresispositivos != null){
            //colocar el adapter
            ecgsAdapter.setDispositivos(nombresispositivos);
            lista.setAdapter(ecgsAdapter);
        }else{
            Toast.makeText(this,"No hay dispositivos",Toast.LENGTH_SHORT);
        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dispositivo = ecgsAdapter.getItem(position);
                //guardar la direccion del dispositivo en la bd
                //verficar que no hayan registros de dispositivos
                ContentResolver rs = getContentResolver();
                Cursor c = null;
                try{
                    c = rs.query(MonitorECGContrato.DispositivoEntry.CONTENT_URI, PROYECCION_DISP, null, null, null);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(c != null){
                    if(c.getCount() != 0){
                        //elimianr los registros
                        int rowsdelete = rs.delete(MonitorECGContrato.DispositivoEntry.CONTENT_URI,null,null);
                    }else{
                        ContentValues cv = new ContentValues();
                        cv.put(MonitorECGContrato.DispositivoEntry.COLUMN_NOMBRE,dispositivo);
                        rs.insert(MonitorECGContrato.DispositivoEntry.CONTENT_URI, cv);
                        Toast.makeText(getBaseContext(),"Se guardo el nombre del ECG",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public ArrayList<String> enlazar() {
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        ArrayList<String> lista = null;
        if (bluetooth == null) {
            Toast.makeText(AgregarElectrocardiografo.this, "Dispositivos bluetooth no disponible", Toast.LENGTH_LONG).show();
        } else {
            if (!bluetooth.isEnabled()) {
                Intent encenderBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(encenderBluetooth, 1);
            }
            dispositivosEnlazados = bluetooth.getBondedDevices();
            lista = new ArrayList<String>();
            if (dispositivosEnlazados.size() > 0) {
                for (BluetoothDevice bt : dispositivosEnlazados) {
                    lista.add(bt.getName() + " " + bt.getAddress());
                }
            }
        }
        return lista;
    }

}
