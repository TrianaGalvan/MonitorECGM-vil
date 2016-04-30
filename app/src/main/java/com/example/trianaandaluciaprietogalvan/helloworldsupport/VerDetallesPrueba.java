package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.FileUtilPrueba;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.NetworkUtil;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.web.ServicioWeb;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class VerDetallesPrueba extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final java.lang.String PARAM_ID = "idPrueba";
    private static final java.lang.String NAME_FILE = "nameFile";
    public final String TAG = "VerDetallePrueba";
    public static final int PRUEBA_DETALLE_LOADER = 2;
    public static final int CARDIOLOGO_LOADER = 3;

    //PROYECCIONES PRUEBA
    public static final String[] PROYECCIONES_PRUEBA_DETALLE = new String[]{
            MonitorECGContrato.PruebaEntry.TABLE_NAME + "." + MonitorECGContrato.PruebaEntry.COLUMN_FECHA,
            MonitorECGContrato.ReporteEntry.TABLE_NAME + "." + MonitorECGContrato.ReporteEntry.COLUMN_RECOMENDACIONES,
            MonitorECGContrato.PruebaEntry.TABLE_NAME + "." + MonitorECGContrato.PruebaEntry.COLUMN_FRECUENCIA_CARDIACA,
            MonitorECGContrato.ReporteEntry.TABLE_NAME + "." + MonitorECGContrato.ReporteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO,
            MonitorECGContrato.PruebaEntry.TABLE_NAME + "." + MonitorECGContrato.PruebaEntry.COLUMN_MUESTRA_COMPLETA

    };


    //PROYECCIONES CARDIOLOGO
    public static final String[] PROYECCIONES_CARDIOLOGO = new String[]{
            MonitorECGContrato.CardiologoEntry.TABLE_NAME + "." + MonitorECGContrato.CardiologoEntry.COLUMN_NOMBRE,
            MonitorECGContrato.CardiologoEntry.TABLE_NAME + "." + MonitorECGContrato.CardiologoEntry.COLUMN_APP,
            MonitorECGContrato.CardiologoEntry.TABLE_NAME + "." + MonitorECGContrato.CardiologoEntry.COLUMN_APM
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
    DownloadTask descargarPrueba;
    ProgressDialog progress;
    String fileName;
    int frecuencia;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        uriPrueba = getIntent().getData();
        if (id == PRUEBA_DETALLE_LOADER) {
            return new CursorLoader(getBaseContext(), uriPrueba, PROYECCIONES_PRUEBA_DETALLE, null, null, null);
        } else if (id == CARDIOLOGO_LOADER) {
            int idCar = args.getInt(BUNDLE_ID_CARDIOLOGO);
            Uri uriCardiologo = MonitorECGContrato.CardiologoEntry.buildCardiologoId(idCar);
            return new CursorLoader(getBaseContext(), uriCardiologo, PROYECCIONES_CARDIOLOGO, null, null, null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == PRUEBA_DETALLE_LOADER) {
            if (data.getCount() != 0) {
                data.moveToFirst();
                fechaECG.setText(data.getString(COLUMN_FECHA));
                recomendaciones.setText(data.getString(COLUMN_RECOMENDACIONES));
                String frecuencia = data.getInt(COLUMN_FRECUENCIA_CARDIACA) + " Ipm";
                frecuenciaCardiaca.setText(frecuencia);
                //obtener el reporte
                int idCardiologo = data.getInt(COLUMN_REPORTE_ID_CARDIOLOGO);

                Bundle bundle = new Bundle();
                bundle.putInt(BUNDLE_ID_CARDIOLOGO, idCardiologo);
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.initLoader(CARDIOLOGO_LOADER, bundle, this);
            }
        }
        if (loader.getId() == CARDIOLOGO_LOADER) {
            if (data.getCount() != 0) {
                data.moveToFirst();
                String nombre = data.getString(COLUMN_NOMBRE_CAR) + " " + data.getString(COLUMN_APP_CAR) + " " + data.getString(COLUMN_APM_CAR);
                medico.setText(nombre);
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onClickVerElectrocardiogramaPaciente(View view) {
        //verificar si el archivo existe
        //verificar si se tiene conexion
        ContentResolver rs = getContentResolver();
        String[] archivo = new String[]{
                MonitorECGContrato.PruebaEntry.TABLE_NAME + "." + MonitorECGContrato.PruebaEntry._ID,
                MonitorECGContrato.PruebaEntry.TABLE_NAME + "." + MonitorECGContrato.PruebaEntry.COLUMN_MUESTRA_COMPLETA,
                MonitorECGContrato.PruebaEntry.TABLE_NAME + "." + MonitorECGContrato.PruebaEntry.COLUMN_FRECUENCIA_CARDIACA
        };
        Cursor cursor = rs.query(uriPrueba, archivo, null, null, null);
        cursor.moveToFirst();
        fileName = cursor.getString(1);
        int id = cursor.getInt(0);
        frecuencia = cursor.getInt(2);
        cursor.close();

        if (fileName != null) {
            //verificar si el archivo existe
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            if (!file.exists()) {
                if (!NetworkUtil.isOnline(this)) {
                    Toast.makeText(this, "Se necesita red para esta operación", Toast.LENGTH_SHORT).show();
                } else {
                    progress = ProgressDialog.show(this, "Descargando prueba", "Espere unos segundos...", true, false);
                    Bundle bundle = new Bundle();
                    bundle.putInt(PARAM_ID,id);
                    bundle.putString(NAME_FILE,fileName);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        descargarPrueba = (DownloadTask) new DownloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bundle);
                    } else {
                        descargarPrueba = (DownloadTask) new DownloadTask().execute(bundle);
                    }
                }
            }else{
                Intent intent = new Intent(getBaseContext(), Grafica.class);
                Bundle bundle = new Bundle();
                bundle.putString(Grafica.PARAM_SERVICE, "ver");
                bundle.putString(Grafica.PARAM_FRECUENCIA, Integer.toString(frecuencia));
                bundle.putString(Grafica.PARAM_NAME_FILE, fileName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    private class DownloadTask extends AsyncTask<Bundle,Void,Boolean> {
        boolean writtenToDisk = false;

        @Override
        protected Boolean doInBackground(Bundle... params) {
            Bundle param = params[0];
            int id = param.getInt(PARAM_ID);
            String fileName = param.getString(NAME_FILE);
            try {
                Response<ResponseBody> respBody = ServicioWeb.descargarPrueba(id);
                writtenToDisk = FileUtilPrueba.writeResponseBodyToDisk(respBody.body(), fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return writtenToDisk;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Boolean res = result;

            if (progress != null) {
                progress.dismiss();
            }
            if(!res){
                Toast.makeText(getBaseContext(),"Ocurrió un error al descargar la prueba",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(getBaseContext(), Grafica.class);
                Bundle bundle = new Bundle();
                bundle.putString(Grafica.PARAM_SERVICE, "ver");
                bundle.putString(Grafica.PARAM_FRECUENCIA, Integer.toString(frecuencia));
                bundle.putString(Grafica.PARAM_NAME_FILE, fileName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
}
