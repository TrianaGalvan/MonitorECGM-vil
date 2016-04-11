package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.web.ServicioWeb;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registrarse extends AppCompatActivity {


    private static final int REGISTRO_OK = 100;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void onClickSiguiente(View view) {

        //verificar si existe el correo
        TextView correo = (TextView) findViewById(R.id.txtCorreo);
        final String sCorreos = correo.getText().toString();
        final int duration = Toast.LENGTH_SHORT;
        ServicioWeb.verfificarCorreo(sCorreos, new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean resp = response.body();
                String text = "El correo esta registrado";
                //ya existe el usuario en el sistema
                if (resp) {
                    Toast toast = Toast.makeText(getBaseContext(), text, duration);
                    toast.show();
                } else {
                    Bundle bundle = new Bundle();
                    TextView nombre = (TextView) findViewById(R.id.txtNombre);
                    bundle.putString(RegistrarDatosMedicos.PARAM_NOMBRE, nombre.getText().toString());

                    TextView app = (TextView) findViewById(R.id.txtApp);
                    bundle.putString(RegistrarDatosMedicos.PARAM_APP, app.getText().toString());

                    TextView apm = (TextView) findViewById(R.id.txtApm);
                    bundle.putString(RegistrarDatosMedicos.PARAM_APM, apm.getText().toString());

                    TextView curp = (TextView) findViewById(R.id.txtCurp);
                    bundle.putString(RegistrarDatosMedicos.PARAM_CURP, curp.getText().toString());

                    TextView edad = (TextView) findViewById(R.id.txtEdad);
                    bundle.putString(RegistrarDatosMedicos.PARAM_EDAD, edad.getText().toString());

                    RadioGroup sexo = (RadioGroup) findViewById(R.id.radioSex);
                    int idSexo = sexo.getCheckedRadioButtonId();
                    RadioButton sexoButton = (RadioButton) findViewById(idSexo);
                    bundle.putString(RegistrarDatosMedicos.PARAM_SEXO, sexoButton.getText().toString());

                    TextView telefono = (TextView) findViewById(R.id.txtTelefono);
                    bundle.putString(RegistrarDatosMedicos.PARAM_TELEFONO, telefono.getText().toString());

                    bundle.putString(RegistrarDatosMedicos.PARAM_CORREO, sCorreos);

                    //contrasena
                    TextView con = (TextView) findViewById(R.id.txtContrasena);
                    bundle.putString(RegistrarDatosMedicos.PARAM_CONTRASENA, con.getText().toString());

                    Intent intentDatosMedicos = new Intent(getBaseContext(), RegistrarDatosMedicos.class);
                    intentDatosMedicos.putExtras(bundle);
                    startActivityForResult(intentDatosMedicos, REGISTRO_OK);

                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTRO_OK){
            if(resultCode == Activity.RESULT_OK){
                finish();
            }
        }
    }
}
