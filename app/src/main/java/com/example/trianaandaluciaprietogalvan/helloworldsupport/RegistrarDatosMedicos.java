package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.adapters.SpinnerCardiologosAdapter;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.CardiologoEntry;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Cardiologo;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Paciente;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.CardiologoDAO;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.HourUtils;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.PacienteDAO;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.web.ServicioWeb;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarDatosMedicos extends AppCompatActivity implements Callback<List<Cardiologo>>{
    public static final String PARAM_NOMBRE = "nombre";
    public static final String PARAM_APP = "app";
    public static final String PARAM_APM = "apm";
    public static final String PARAM_CURP = "curp";
    public static final String PARAM_EDAD = "edad";
    public static final String PARAM_SEXO = "sexo";
    public static final String PARAM_TELEFONO = "telefono";
    public static final String PARAM_CORREO = "correo";
    public static final String PARAM_CONTRASENA = "contrasena";

    //Proyeccion para verificar si el cardiologo ya existe
    public static final String[] PROYECCION_VERIFICAR_CARDIOLOGO = new String[]{
        CardiologoEntry.TABLE_NAME+"."+CardiologoEntry._ID
    };
    //Columna del id del cardiologo
    public static final int COLUMN_ID_CARDIOLOGO = 0;

    //obtener las vistas con la libreria Butter Knife
    @Bind(R.id.txtFrecuencia)
    EditText frecuenciaCardiaca;
    @Bind(R.id.txtPresionS)
    EditText presionSistolica;
    @Bind(R.id.txtPresionD)
    EditText presionDiastolica;
    @Bind(R.id.txtAltura)
    EditText altura;
    @Bind(R.id.txtPeso)
    EditText peso;
    @Bind(R.id.cardiologos)
    Spinner cardiologo;

    SpinnerCardiologosAdapter sca = null;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registrar_datos_medicos);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        //obtener el bundle
        bundle = getIntent().getExtras();

        sca = new SpinnerCardiologosAdapter(this);
        //asignar al spinner el adapter
        cardiologo.setAdapter(sca);

        //obtener medicos
        ServicioWeb.obtenerCardiologos(this);


    }

    @Override
    public void onResponse(Call<List<Cardiologo>> call, Response<List<Cardiologo>> response) {
        if(response.isSuccessful()){
            List<Cardiologo> lista = response.body();
            sca.setListaCardiologos(lista);
        }
        else{

        }
    }

    @Override
    public void onFailure(Call<List<Cardiologo>> call, Throwable t) {

    }

    public void onClickRegistrarseFinal(View view){
        final ContentResolver rs = getContentResolver();


        String sFrecuencia = frecuenciaCardiaca.getText().toString();
        String sPresionSistolica = presionSistolica.getText().toString();
        String sPresionDiastolica = presionDiastolica.getText().toString();
        String sAltura = altura.getText().toString();
        String sPeso = peso.getText().toString();
        final Cardiologo car  = (Cardiologo) cardiologo.getSelectedItem();

        //paciente
        Paciente paciente = new Paciente();
        paciente.nombre = bundle.getString(PARAM_NOMBRE);
        paciente.apellidoPaterno = bundle.getString(PARAM_APP);
        paciente.apellidoMaterno = bundle.getString(PARAM_APM);
        paciente.curp = bundle.getString(PARAM_CURP);
        String edad = bundle.getString(PARAM_EDAD);
        paciente.edad = bundle.getString(PARAM_EDAD).isEmpty() ? 0 : Integer.parseInt(bundle.getString(PARAM_EDAD));
        paciente.correo = bundle.getString(PARAM_CORREO);
        paciente.telefono = bundle.getString(PARAM_TELEFONO);
        paciente.sexo = bundle.getString(PARAM_SEXO).isEmpty() ? 'N' :bundle.getString(PARAM_SEXO).charAt(0);
        paciente.contrasena = bundle.getString(PARAM_CONTRASENA);

        paciente.frecuenciaRespiratoria = sFrecuencia.isEmpty() ? 0 : Integer.parseInt(sFrecuencia);
        paciente.presionSistolica = sPresionSistolica.isEmpty() ? 0: Integer.parseInt(sPresionSistolica);
        paciente.presionDiastolica = sPresionDiastolica.isEmpty() ? 0: Integer.parseInt(sPresionDiastolica);
        paciente.altura = sAltura.isEmpty() ? 0 : Double.parseDouble(sAltura);
        paciente.peso = sPeso.isEmpty() ? 0 : Integer.parseInt(sPeso);
        paciente.cardiologo = car;
        //calcular el imc
        if(paciente.peso != 0 && paciente.altura != 0){
            paciente.imc = paciente.peso / Math.pow(paciente.altura,2);
        }else{
            paciente.imc = 0;
        }

        //obtener la fecha
        String date = HourUtils.getDate();
        paciente.fechamodificacion = date;

        ServicioWeb.insertarPaciente(paciente, new Callback<Paciente>() {
            @Override
            public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                if(response.isSuccessful()){
                    //insertar en la base de datos
                    //verificar si ya existe el cardiologo registrado en la bd
                    verificarRegistroMedico(car,rs);
                    Paciente p = response.body();
                    if(p == null){
                        Toast.makeText(getBaseContext(),"Ocurrió un error en el servidor",Toast.LENGTH_SHORT).show();
                    }else{
                        PacienteDAO.insertarPaciente(p, rs);
                        Toast.makeText(RegistrarDatosMedicos.this, "Fuiste registrado con éxito", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }
                else{
                    Toast.makeText(RegistrarDatosMedicos.this, "Ocurrió  un error en el sistema", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Paciente> call, Throwable t) {

            }
        });
    }


    public void verificarRegistroMedico(Cardiologo car,ContentResolver rs){
        Uri uriCardiologoId = CardiologoEntry.buildCardiologoId(car.idCardiologo);
        Cursor cursor = rs.query(uriCardiologoId, PROYECCION_VERIFICAR_CARDIOLOGO, null, null, null);
        //verificar si el cursor tiene datos
        int count = cursor.getCount();
        //no existe el cardiologo regstrado en la bd
        if(count == 0){
            CardiologoDAO.insertarCardiologo(car, rs);
        }
    }


}
