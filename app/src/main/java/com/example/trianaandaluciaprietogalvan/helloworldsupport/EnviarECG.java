package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.sync.MonitorECGSync;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.AccountUtil;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.NetworkUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EnviarECG extends AppCompatActivity {

    @Bind(R.id.editFechaHora)
    EditText fechaHora;
    @Bind(R.id.editMedico)
    EditText medico;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    final String SELECCION_CARDIOLOGO = MonitorECGContrato.CardiologoEntry.TABLE_NAME+"."+
                                        MonitorECGContrato.CardiologoEntry._ID+"= ?";

    String[] PROYECCION_CARDIOLOGO = new String[]{
        MonitorECGContrato.CardiologoEntry.COLUMN_NOMBRE,
        MonitorECGContrato.CardiologoEntry.COLUMN_APP,
        MonitorECGContrato.CardiologoEntry.COLUMN_APM
    };

    String[] PROYECCION_PACIENTE = new String[]{
        MonitorECGContrato.PacienteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO
    };


    final int COLUMN_ID_PACIENTE = 0;

    final int COLUMN_NOMBRE_CARDIOLOGO = 0;
    final int COLUMN_APP_CARDIOLOGO= 1;
    final int COLUMN_APM_CARDIOLOGO= 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_ecg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ButterKnife.bind(this);
        //obtener los campos de médicos y la fecha
        obtenerCamposInterfaz();
    }

    private void obtenerCamposInterfaz() {
        //obtener el campo medico
        ContentResolver rs = getContentResolver();

        //obtener el id del paciente
        Cursor cursor = rs.query(MonitorECGContrato.PacienteEntry.CONTENT_URI, PROYECCION_PACIENTE, null, null, null);
        cursor.moveToFirst();
        int id = cursor.getInt(COLUMN_ID_PACIENTE);

        String[] ARGS_PROYECCION_CARDIOLOGO = new String[]{
            Integer.toString(id)
        };

        Cursor cursorcar = rs.query(MonitorECGContrato.CardiologoEntry.CONTENT_URI, PROYECCION_CARDIOLOGO, SELECCION_CARDIOLOGO, ARGS_PROYECCION_CARDIOLOGO, null);
        cursorcar.moveToFirst();
        String nombre = cursorcar.getString(COLUMN_NOMBRE_CARDIOLOGO) + " "+ cursorcar.getString(COLUMN_APP_CARDIOLOGO)+" "+cursorcar.getString(COLUMN_APM_CARDIOLOGO);
        medico.setText(nombre);

        //obtener la hora actual
        //obtener la fecha
        String date = getDate();
        fechaHora.setText(date);

    }

    public String getDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd     HH:mm");
        return sdf.format(c.getTime());
    }


    public void onClickEnviarECG(View view) {
        //se verifica si hay red
        boolean net = NetworkUtil.isOnline(this);
        if (net) {
            //verificar el tipo de conexion
            boolean mobileNet = NetworkUtil.isConnctedToMobileNet(this);
            if (mobileNet) {
                showDialog();
            } else {
                enviarGrafica(AccountUtil.getAccount(getBaseContext()));
            }
        } else {
            Toast.makeText(this, "No hay red, se actualizará en cuanto se tenga conexión", Toast.LENGTH_SHORT);
        }
    }


    public void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Tipo de conexión");

        // set dialog message
        alertDialogBuilder
                .setMessage("Se actualizará tu electrocardiograma con una red móvil")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enviarGrafica(AccountUtil.getAccount(getBaseContext()));
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


    public void enviarGrafica(Account account){
        Bundle bundle = new Bundle();
        bundle.putInt(MonitorECGSync.SINCRONIZACION,MonitorECGSync.SINCRONIZACION_ELECTROCARDIOGRAMA);
        MonitorECGSync.syncInmediatly(getBaseContext(),account,bundle);
    }

    public void onClickCancelarECG(View view) {
    }
}
