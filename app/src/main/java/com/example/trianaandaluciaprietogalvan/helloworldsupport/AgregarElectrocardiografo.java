package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AgregarElectrocardiografo extends AppCompatActivity {
    String[] electrocardiografos = null;

    @Bind(R.id.lista_electrocardiogramas)
    ListView lista;

    public AgregarElectrocardiografo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_agregar_electrocardiografo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ButterKnife.bind(this);

        electrocardiografos = getResources().getStringArray(R.array.electrocardiogramas);
        lista.setAdapter(new ArrayAdapter<String>(this, R.layout.electrocardiograma_row, electrocardiografos));
    }
}
