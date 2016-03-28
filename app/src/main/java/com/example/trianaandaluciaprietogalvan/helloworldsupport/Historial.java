package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;


public class Historial extends Fragment {
    public Historial() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView lista = null;

        final HistorialItem rows_data[] = new HistorialItem[]
                {
                        new HistorialItem("01/12/2015","Pendiente"),
                        new HistorialItem("01/12/2015","Revisado"),
                        new HistorialItem("05/10/2015","Revisado"),
                        new HistorialItem("10/11/2015","No revisado")
                };
        //obtener el contexto del fragmento
        FragmentActivity fragmentActivity =  getActivity();
        //crear el adapter para la lista del menu
        HistorialAdapter adapter = new HistorialAdapter(fragmentActivity,
                R.layout.historial_row,rows_data);

        FrameLayout frame = (FrameLayout)getView();


        if(frame != null) {
            lista = (ListView)frame.findViewById(R.id.lista_historial);
            lista.setAdapter(adapter);
        }

        FloatingActionButton fab = (FloatingActionButton) frame.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                Electrocardiograma ecg = new Electrocardiograma();
                transaction.replace(R.id.content_frame,ecg);
                transaction.addToBackStack(null);
                FragmentActivity activity = getActivity();
                activity.setTitle("Electrocardiograma");

                // Commit the transaction
                transaction.commit();
            }
        });

        //manejar el evento de click en un item de la lista
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistorialItem item = rows_data[position];
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                //open Intent
                VerElectrocardiograma ve = new VerElectrocardiograma();
                FragmentActivity activity = getActivity();
                // Insert the fragment by replacing any existing fragment
                ft.replace(R.id.content_frame,ve);
                ft.commit();

                activity.setTitle("Ver electrocardiograma");
            }
        });
    }
}
