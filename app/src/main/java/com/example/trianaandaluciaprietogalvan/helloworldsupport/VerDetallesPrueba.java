package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VerDetallesPrueba extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int PRUEBA_DETALLE_LOADER = 2;
    public static final int CARDIOLOGO_LOADER = 3;

    //PROYECCIONES PRUEBA
    public static final String[] PROYECCIONES_PRUEBA_DETALLE = new String[]{
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry.COLUMN_FECHA,
            MonitorECGContrato.ReporteEntry.TABLE_NAME+"."+MonitorECGContrato.ReporteEntry.COLUMN_RECOMENDACIONES,
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry.COLUMN_FRECUENCIA_CARDIACA,
            MonitorECGContrato.ReporteEntry.TABLE_NAME+"."+ MonitorECGContrato.ReporteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO,
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+ MonitorECGContrato.PruebaEntry.COLUMN_MUESTRA_COMPLETA

    };


    //PROYECCIONES CARDIOLOGO
    public static final String[] PROYECCIONES_CARDIOLOGO = new String[]{
            MonitorECGContrato.CardiologoEntry.TABLE_NAME+"."+MonitorECGContrato.CardiologoEntry.COLUMN_NOMBRE,
            MonitorECGContrato.CardiologoEntry.TABLE_NAME+"."+MonitorECGContrato.CardiologoEntry.COLUMN_APP,
            MonitorECGContrato.CardiologoEntry.TABLE_NAME+"."+MonitorECGContrato.CardiologoEntry.COLUMN_APM
    };

    //Bundle
    public static final String BUNDLE_ID_CARDIOLOGO = "idCardiologo";

    //Columnas de las proyecciones
    public static final int COLUMN_FECHA = 0;
    public static final int COLUMN_RECOMENDACIONES = 1;
    public static final int COLUMN_FRECUENCIA_CARDIACA = 2;
    public static final int COLUMN_REPORTE_ID_CARDIOLOGO = 3;
    public static final int COLUMN_REPORTE_ID_MUESTRA_NOMBRE = 4;

    //PROYECCIONES DE CARDIOLOGO
    public static final int COLUMN_NOMBRE_CAR = 0;
    public static final int COLUMN_APP_CAR = 1;
    public static final int COLUMN_APM_CAR = 2;


    @Bind(R.id.txtMedico)
    TextView medico;
    @Bind(R.id.txtFrecuenciaCardiaca)
    TextView frecuenciaCardiaca;
    @Bind(R.id.txtFechaECG)
    TextView fechaECG;
    @Bind(R.id.txtRecomendaciones)
    TextView recomendaciones;

    Uri uriPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalles_prueba);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Inicializar los loaders
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(PRUEBA_DETALLE_LOADER, null, this);

    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        uriPrueba = getIntent().getData();
        if(id == PRUEBA_DETALLE_LOADER){
            return  new CursorLoader(getBaseContext(),uriPrueba,PROYECCIONES_PRUEBA_DETALLE,null,null,null);
        }else if(id == CARDIOLOGO_LOADER){
            int idCar = args.getInt(BUNDLE_ID_CARDIOLOGO);
            Uri uriCardiologo = MonitorECGContrato.CardiologoEntry.buildCardiologoId(idCar);
            return new CursorLoader(getBaseContext(),uriCardiologo,PROYECCIONES_CARDIOLOGO,null,null,null);
        }else{
            return  null;
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == PRUEBA_DETALLE_LOADER){
            if(data.getCount() != 0){
                data.moveToFirst();
                fechaECG.setText(data.getString(COLUMN_FECHA));
                recomendaciones.setText(data.getString(COLUMN_RECOMENDACIONES));
                String frecuencia = data.getInt(COLUMN_FRECUENCIA_CARDIACA)+" Ipm";
                frecuenciaCardiaca.setText(frecuencia);
                //obtener el reporte
                int idCardiologo = data.getInt(COLUMN_REPORTE_ID_CARDIOLOGO);

                Bundle bundle = new Bundle();
                bundle.putInt(BUNDLE_ID_CARDIOLOGO,idCardiologo);
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.initLoader(CARDIOLOGO_LOADER,bundle, this);
            }
        }
        if(loader.getId() == CARDIOLOGO_LOADER){
            if(data.getCount() != 0){
                data.moveToFirst();
                String nombre = data.getString(COLUMN_NOMBRE_CAR) + " " + data.getString(COLUMN_APP_CAR) + " " + data.getString(COLUMN_APM_CAR);
                medico.setText(nombre);
            }
        }
    }


    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    public void onClickVerElectrocardiograma(View view) {
        ContentResolver rs = getContentResolver();
        String[] archivo = new String[]{
                MonitorECGContrato.PruebaEntry.COLUMN_MUESTRA_COMPLETA
        };
        Cursor cursor = rs.query(uriPrueba, archivo, null, null, null);
        cursor.moveToFirst();
        String fileName = cursor.getString(0);

        //eliminar el archivo existente
        File file = new File(Environment.getExternalStorageDirectory(),fileName);
        if(file.exists()){

        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(this,Grafica.class);
        startActivity(intent);
    }
}
