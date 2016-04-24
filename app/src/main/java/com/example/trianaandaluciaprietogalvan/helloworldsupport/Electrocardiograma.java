package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.NetworkUtil;


public class Electrocardiograma extends AppCompatActivity {
    public Electrocardiograma() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrocardiograma);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void onClickEmpezarGrafica(View view) {
        //verificar si hay red
        boolean net = NetworkUtil.isOnline(this);
        if(!net){
            Toast.makeText(this,"Los datos se actualizaran cuando haya red",Toast.LENGTH_SHORT);
        }
        Intent inten = new Intent(this,Grafica.class);
        startActivity(inten);
    }
}
