package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class loginFinal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_final);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClickRecuperarcontrasena(View view) {
        Intent intentRecuperarContrasena = new Intent();
        intentRecuperarContrasena.setClass(this,RecuperarContrasena.class);
        startActivity(intentRecuperarContrasena);
    }

    public void onClickEntrarAplicacion(View view) {
        Intent intentAplicacion = new Intent();
        intentAplicacion.setClass(this,MainActivity.class);
        startActivity(intentAplicacion);
    }
}
