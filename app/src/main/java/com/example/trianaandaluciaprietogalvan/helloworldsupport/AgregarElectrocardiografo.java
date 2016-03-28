package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class AgregarElectrocardiografo extends Fragment {
    String[] electrocardiografos = null;

    public AgregarElectrocardiografo() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agregar_electrocardiografo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        electrocardiografos = getResources().getStringArray(R.array.electrocardiogramas);
        FragmentActivity activity =  getActivity();
        ListView lista = (ListView) activity.findViewById(R.id.lista_electrocardiogramas);
        lista.setAdapter(new ArrayAdapter<String>(activity,R.layout.electrocardiograma_row,electrocardiografos));
    }
}
