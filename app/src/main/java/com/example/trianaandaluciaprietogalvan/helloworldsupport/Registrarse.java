package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.web.ServicioWeb;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registrarse extends AppCompatActivity {


    private static final int REGISTRO_OK = 100;

    @Bind(R.id.txtNombre)
    EditText nombre;
    @Bind(R.id.txtApp)
    EditText app;
    @Bind(R.id.txtApm)
    EditText apm;
    @Bind(R.id.txtCorreo)
    EditText correo;
    @Bind(R.id.txtEdad)
    EditText edad;
    @Bind(R.id.txtContrasena)
    EditText pass;
    @Bind(R.id.txtConfirmarContrasena)
    EditText confPass;

    @OnTextChanged(R.id.txtNombre) void onNombreChange(CharSequence text) {
        nombre.setError(null);
    }@OnTextChanged(value = R.id.txtNombre, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED) void onAfterNombreChange(CharSequence text) {
        String pattern= "^[a-zA-Z]*$";
        if(!nombre.getText().toString().matches(pattern)){
            nombre.setError("Sólo se aceptan caractéres");
        }
    }
    @OnTextChanged(R.id.txtApp) void onApp(CharSequence text) {
        app.setError(null);
    }@OnTextChanged(R.id.txtApm) void onChangeApm(CharSequence text) {
        apm.setError(null);
    }@OnTextChanged(R.id.txtCorreo) void onChangeCorreo(CharSequence text) {
        correo.setError(null);
    }@OnTextChanged( value = R.id.txtCorreo,callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED) void onVerificarCorreo(CharSequence text) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        boolean match = pattern.matcher(correo.getText().toString()).matches();
        if(!match){
            correo.setError("Correo inválido");
        }
    }@OnTextChanged(R.id.txtEdad) void onChangeEdad(CharSequence text) {
        edad.setError(null);
    }@OnTextChanged(R.id.txtContrasena) void onChangeContrasena(CharSequence text) {
        pass.setError(null);
    }@OnTextChanged(value = R.id.txtConfirmarContrasena, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED) void onAtferConfirmarContrasena(CharSequence text) {
        if(!pass.getText().toString().equals(confPass.getText().toString())){
            confPass.setError("Las contraseñas no coinciden");
        }else{
            confPass.setError(null);
        }
    }




    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void onClickSiguiente(View view) {
        //verificar los campos

        boolean bandera = verificarCampos();
        if (bandera){
            //verificar si existe el correo
            TextView correo = (TextView) findViewById(R.id.txtCorreo);final String sCorreos = correo.getText().toString();
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
                        if (sexoButton != null) {
                            bundle.putString(RegistrarDatosMedicos.PARAM_SEXO, sexoButton.getText().toString());
                        } else {
                            bundle.putString(RegistrarDatosMedicos.PARAM_SEXO, "");
                        }

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTRO_OK){
            if(resultCode == Activity.RESULT_OK){
                finish();
            }
        }
    }

    public boolean verificarCampos(){
        boolean bandera = true;
        if(nombre.getText().toString().length() == 0){
            bandera = false;
            nombre.setError(Html.fromHtml("<font color='white'>Requerido</font>"));
        }if(app.getText().toString().length() == 0){
            bandera = false;
            app.setError(Html.fromHtml("<font color='white'>Requerido</font>"));
        }if(apm.getText().toString().length() == 0){
            bandera = false;
            apm.setError(Html.fromHtml("<font color='white'>Requerido</font>"));
        }if(correo.getText().toString().length() == 0){
            bandera = false;
            correo.setError(Html.fromHtml("<font color='white'>Requerido</font>"));
        }if(edad.getText().toString().length() == 0){
            bandera = false;
            edad.setError(Html.fromHtml("<font color='white'>Requerido</font>"));
        }if(pass.getText().toString().length() == 0){
            bandera = false;
            pass.setError(Html.fromHtml("<font color='white'>Requerido</font>"));
        }if(confPass.getText().toString().length() == 0){
            bandera = false;
            confPass.setError(Html.fromHtml("<font color='white'>Requerido</font>"));
        }
        return  bandera;
    }
}
