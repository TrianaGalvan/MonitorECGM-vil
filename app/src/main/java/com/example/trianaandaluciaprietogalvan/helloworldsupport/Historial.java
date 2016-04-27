package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.accounts.Account;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Prueba;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Reporte;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.sync.MonitorECGSync;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.AccountUtil;
import java.util.ArrayList;
import java.util.List;


public class Historial extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    //@Bind(R.id.linearNoHayPruebas)
    //LinearLayout pruebaExistencias;

    FragmentActivity activity = getActivity();

    public static final int PRUEBA_LOADER = 1;

    public static final String[] PROYECCIONES_PRUEBA = new String[]{
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+ MonitorECGContrato.PruebaEntry._ID,
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry.COLUMN_FECHA,
            MonitorECGContrato.ReporteEntry.TABLE_NAME+"."+MonitorECGContrato.ReporteEntry.COLUMN_ESTATUS
    };

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_FECHA = 1;
    public static final int COLUMN_ESTATUS = 2;

    HistorialAdapter adapter;


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

        //Obtener pruebas
        obtenerPruebas(AccountUtil.getAccount(getContext()));

        //Implementando LOADERS
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(PRUEBA_LOADER, null, this);

        ListView lista = null;

        //obtener el contexto del fragmento
        FragmentActivity fragmentActivity =  getActivity();
        //crear el adapter para la lista del menu
        adapter = new HistorialAdapter(fragmentActivity,
                R.layout.historial_row);

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
                Intent intent = new Intent(getContext(),Grafica.class);
                startActivity(intent);
            }
        });

        //manejar el evento de click en un item de la lista
        assert lista != null;

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //obtener el item del adapter
                Prueba pruebaSeleccionada = (Prueba) adapter.getItem(position);

                Uri uriPrueba = MonitorECGContrato.PruebaEntry.buildPruebaId(pruebaSeleccionada.idPrueba);

                Intent intentDetalle = new Intent(getContext(), VerDetallesPrueba.class);
                intentDetalle.setData(uriPrueba);
                startActivity(intentDetalle);
            }
        });
    }

    public void obtenerPruebas(Account account){
        Bundle bundle = new Bundle();
        bundle.putString(MonitorECGSync.PARAM_EMAIL,account.name);
        bundle.putInt(MonitorECGSync.SINCRONIZACION,MonitorECGSync.SINCRONIZACION_PRUEBA);

        MonitorECGSync.syncInmediatly(getContext(),account,bundle);
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == PRUEBA_LOADER){
            return  new CursorLoader(getContext(), MonitorECGContrato.PruebaEntry.CONTENT_URI, PROYECCIONES_PRUEBA,null,null,null);
        }else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        LinearLayout pruebaExistencias = (LinearLayout) getActivity().findViewById(R.id.linearNoHayPruebas);
        pruebaExistencias.setVisibility(View.INVISIBLE);

        if(loader.getId() == PRUEBA_LOADER){
            if(data.getCount() != 0){
                List<Prueba> pruebas = new ArrayList<>();
                data.moveToFirst();
                do {
                    Prueba prueba = new Prueba();
                    prueba.reporte = new Reporte();
                    prueba.idPrueba = data.getInt(COLUMN_ID);
                    prueba.fecha = data.getString(COLUMN_FECHA);
                    prueba.reporte.estatus = data.getInt(COLUMN_ESTATUS);
                    pruebas.add(prueba);
                }while (data.moveToNext());
                //poner al adapter las pruebas que se mostraran
                adapter.setPruebas(pruebas);
            }else{
                pruebaExistencias.setVisibility(View.VISIBLE);
            }
        }
        else{

        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        adapter.setPruebas(null);
    }
}
