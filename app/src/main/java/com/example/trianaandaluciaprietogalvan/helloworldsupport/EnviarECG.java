package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.AccountUtil;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.HourUtils;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.NetworkUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EnviarECG extends AppCompatActivity {

    public static final String PARAM_FREC = "frecuencia";
    public static final String PARAM_NOMBRE_ARCHIVO = "nombreArch";

    //SELECCION
    public static final String SELECCION_PRUEBA = MonitorECGContrato.PruebaEntry.TABLE_NAME+
                                            "."+ MonitorECGContrato.PruebaEntry.COLUMN_UPDATE+"=1";

    @Bind(R.id.editFecha)
    EditText fecha;
    @Bind(R.id.editHora)
    EditText hora;
    @Bind(R.id.editObervaciones)
    EditText observaciones;
    @Bind(R.id.editMedico)
    EditText medico;

    //ide del paciente
    int ID_PACIENTE;
    int ID_CARDIOLOGO;

    String NOMBRE_ARCHIVO;
    String FRECUENCIA;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    final String SELECCION_CARDIOLOGO = MonitorECGContrato.CardiologoEntry.TABLE_NAME+"."+
                                        MonitorECGContrato.CardiologoEntry._ID+"= ?";

    String[] PROYECCION_CARDIOLOGO = new String[]{
        MonitorECGContrato.CardiologoEntry.COLUMN_NOMBRE,
        MonitorECGContrato.CardiologoEntry.COLUMN_APP,
        MonitorECGContrato.CardiologoEntry.COLUMN_APM,
            MonitorECGContrato.CardiologoEntry._ID
    };

    String[] PROYECCION_PACIENTE = new String[]{
        MonitorECGContrato.PacienteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO
    };


    final int COLUMN_ID_PACIENTE = 0;

    final int COLUMN_NOMBRE_CARDIOLOGO = 0;
    final int COLUMN_APP_CARDIOLOGO= 1;
    final int COLUMN_APM_CARDIOLOGO= 2;
    final int COLUMN_ID_CARDIOLOGO= 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_ecg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ButterKnife.bind(this);
        //obtener la frecuencia y nombre del archivo
        Bundle b = getIntent().getExtras();
        NOMBRE_ARCHIVO = b.getString(PARAM_NOMBRE_ARCHIVO);
        FRECUENCIA = b.getString(PARAM_FREC);

        //obtener los campos de médicos y la fecha
        obtenerCamposInterfaz();

    }

    private void obtenerCamposInterfaz() {
        //obtener el campo medico
        ContentResolver rs = getContentResolver();

        //obtener el id del paciente
        Cursor cursor = rs.query(MonitorECGContrato.PacienteEntry.CONTENT_URI, PROYECCION_PACIENTE, null, null, null);
        cursor.moveToFirst();
        ID_PACIENTE = cursor.getInt(COLUMN_ID_PACIENTE);

        String[] ARGS_PROYECCION_CARDIOLOGO = new String[]{
            Integer.toString(ID_PACIENTE)
        };

        Cursor cursorcar = rs.query(MonitorECGContrato.CardiologoEntry.CONTENT_URI, PROYECCION_CARDIOLOGO, SELECCION_CARDIOLOGO, ARGS_PROYECCION_CARDIOLOGO, null);
        cursorcar.moveToFirst();
        ID_CARDIOLOGO = cursorcar.getInt(COLUMN_ID_CARDIOLOGO);
        String nombre = cursorcar.getString(COLUMN_NOMBRE_CARDIOLOGO) + " "+ cursorcar.getString(COLUMN_APP_CARDIOLOGO)+" "+cursorcar.getString(COLUMN_APM_CARDIOLOGO);
        medico.setText(nombre);

        //obtener la hora actual
        //obtener la fecha
        String date = HourUtils.getDateHour();
        String[] vals = date.split(" ");
        fecha.setText(vals[0]);
        hora.setText(vals[1]);

    }

    public void onClickEnviarECG(View view) {
        //se verifica si hay red
        boolean net = NetworkUtil.isOnline(this);
        if (net) {
            //verificar el tipo de conexion
            boolean mobileNet = NetworkUtil.isConnctedToMobileNet(this);
            crearPrueba();
            Toast.makeText(this, "Se creó la prueba correctamente", Toast.LENGTH_SHORT).show();
        } else {
            crearPrueba();
            Toast.makeText(this, "No hay red, se actualizará en cuanto se tenga conexión", Toast.LENGTH_SHORT).show();
        }
        enviarGrafica(AccountUtil.getAccount(getBaseContext()));
    }

    private void crearPrueba() {
        ContentResolver rs = getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(MonitorECGContrato.PruebaEntry.COLUMN_FECHA,fecha.getText().toString());
        cv.put(MonitorECGContrato.PruebaEntry.COLUMN_HORA,hora.getText().toString());
        cv.put(MonitorECGContrato.PruebaEntry.COLUMN_OBSERVACIONES, observaciones.getText().toString());
        cv.put(MonitorECGContrato.PruebaEntry.COLUMN_PACIENTE_ID_PACIENTE,ID_PACIENTE);
        cv.put(MonitorECGContrato.PruebaEntry.COLUMN_UPDATE,1);
        cv.put(MonitorECGContrato.PruebaEntry.COLUMN_MUESTRA_COMPLETA, NOMBRE_ARCHIVO);
        String[] vals = FRECUENCIA.split(" ");
        cv.put(MonitorECGContrato.PruebaEntry.COLUMN_FRECUENCIA_CARDIACA,(int)Float.parseFloat(vals[0]));
        rs.insert(MonitorECGContrato.PruebaEntry.CONTENT_URI,cv);
    }
    


    public void enviarGrafica(Account account){
        Intent inten = new Intent(getBaseContext(),MainActivity.class);
        startActivity(inten);
    }

    public void onClickCancelarECG(View view) {
        File file = new File(Environment.getExternalStorageDirectory(),NOMBRE_ARCHIVO);
        file.delete();
        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        ContentResolver rs = getContentResolver();
        rs.delete(MonitorECGContrato.PruebaEntry.CONTENT_URI,SELECCION_PRUEBA,null);
        startActivity(intent);
    }
}
