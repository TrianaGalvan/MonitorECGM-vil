package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Registrarse extends AppCompatActivity {


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
        Intent intentDatosClinicos = new Intent();
        intentDatosClinicos.setClass(this, RegistrarDatosMedicos.class);
        startActivity(intentDatosClinicos);
    }

}
