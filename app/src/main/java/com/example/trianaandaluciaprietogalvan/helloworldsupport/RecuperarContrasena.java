package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.web.ServicioWeb;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarContrasena extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void onRecuperarContrasena(View view) {
        TextView emailtxt = (TextView) findViewById(R.id.txtCorreo);
        String email =  emailtxt.getText().toString();
        final int duration = Toast.LENGTH_SHORT;
        Context context = getApplicationContext();
        CharSequence text;
        //verificar que exista red
        boolean online = isOnline();
        if(online){
            //verificar que exista el correo en el sistema
                ServicioWeb.verfificarCorreo(email, new Callback<Boolean>() {
                String text;
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Boolean resp = response.body();
                    //si existe el correo y se envio el correo
                    if(resp){
                        text ="Se te a enviado un correo";
                    }
                    //no existe el correo
                    else{
                        text = "El correo no esta registrado en el sistema";
                    }
                    Toast toast = Toast.makeText(getBaseContext(), text,duration);
                    toast.show();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });
        }else{
            text ="Verifica tu conexión a internet";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
