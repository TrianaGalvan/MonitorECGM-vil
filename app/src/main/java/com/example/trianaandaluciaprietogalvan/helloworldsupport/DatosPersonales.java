package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContentProvider;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Paciente;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.AccountUtil;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.PacienteDAO;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DatosPersonales extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @Bind(R.id.txtNombreDP)
    EditText nombre;
    @Bind(R.id.txtSexoDP)
    EditText sexo;
    @Bind(R.id.txtCurpDP)
    EditText curp;
    @Bind(R.id.txtCorreoDP)
    EditText correo;
    @Bind(R.id.txtTelefonoDP)
    EditText telefono;
    @Bind(R.id.txtEdadDP)
    EditText edadPersonales;
    @Bind(R.id.layoutRadios)
    LinearLayout radios;
    @Bind(R.id.radioFemenino)
    RadioButton radioF;
    @Bind(R.id.radioMasculino)
    RadioButton radioM;
    @Bind(R.id.fab_editar)
    FloatingActionButton fab;
    @Bind(R.id.fab_ok)
    FloatingActionButton fabok;
    @Bind(R.id.radioSex)
    RadioGroup radiogroup;

    //COLUMNS PROYECCION DE PACIENTE
    public static final int COLUMN_NOMBRE = 0;
    public static final int COLUMN_APP = 1;
    public static final int COLUMN_APM = 2;
    public static final int COLUMN_CURP = 3;
    public static final int COLUMN_EDAD = 4;
    public static final int COLUMN_SEXO = 5;
    public static final int COLUMN_CORREO = 6;
    public static final int COLUMN_TELEFONO = 7;
    public static final int COLUMN_ID = 8;

    public static final int DATOS_PERSONALES_LOADER = 1;

    //PACIENTE A ACTUALIZAR
    public  Paciente pacienteActualizar;

    //WHERE PARA ACTUALIZAR PACIENTE
    public static final String WHERE_ID_PACIENTE  = MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+
                                                    MonitorECGContrato.PacienteEntry._ID +"=?";


    public DatosPersonales() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_datos_personales, container, false);
        ButterKnife.bind(this, view);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(DATOS_PERSONALES_LOADER, null, this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabok.setVisibility(View.VISIBLE);
                fab.setVisibility(View.INVISIBLE);
                setEditable(true);

            }
        });

        fabok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabok.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
                setEditable(false);
                actualizarDatosPersonales(AccountUtil.getAccount(getContext()));
            }
        });
    }

    public void actualizarDatosPersonales(Account account){
        ContentValues cv = new ContentValues();

        String nombres  = nombre.getText().toString();
        String[] nombresArray = nombres.split(" ");
        String nombre = "";
        if(nombresArray.length > 3){
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_NOMBRE,nombresArray[0]+" "+nombresArray[1]);
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_APP,nombresArray[2]);
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_APM,nombresArray[3]);
        }else {
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_NOMBRE,nombresArray[0]);
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_APP,nombresArray[1]);
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_APM,nombresArray[2]);
        }
        cv.put(MonitorECGContrato.PacienteEntry.COLUMN_EDAD,Integer.parseInt(edadPersonales.getText().toString()));

        int idSexo = radiogroup.getCheckedRadioButtonId();
        String sexo;
        if(idSexo == radioF.getId()){
            sexo = "F";
        }else if(idSexo == radioM.getId()){
            sexo = "M";
        }else {
            sexo = "N";
        }
        cv.put(MonitorECGContrato.PacienteEntry.COLUMN_SEXO,sexo);
        cv.put(MonitorECGContrato.PacienteEntry.COLUMN_CURP,curp.getText().toString());
        cv.put(MonitorECGContrato.PacienteEntry.COLUMN_CORREO,correo.getText().toString());
        cv.put(MonitorECGContrato.PacienteEntry.COLUMN_TELEFONO, telefono.getText().toString());

        ContentResolver rs = getContext().getContentResolver();

        String[] argumentos_seleccion = new String[]{
                Integer.toString(pacienteActualizar.idPaciente)
        };

        cv.put(MonitorECGContrato.PacienteEntry.BANDERA_ACTUALIZAR,1);

        Uri.Builder builder = MonitorECGContrato.PacienteEntry.CONTENT_URI.buildUpon();
        Uri sync_update_uri = builder.appendQueryParameter(MonitorECGContentProvider.QUERY_SYNC, "true").build();

        rs.update(sync_update_uri, cv, WHERE_ID_PACIENTE, argumentos_seleccion);

    }

    public void setEditable(boolean visibility){
        nombre.setFocusable(visibility);
        nombre.setFocusableInTouchMode(visibility);
        nombre.setClickable(visibility);

        curp.setFocusable(visibility);
        curp.setFocusableInTouchMode(visibility);
        curp.setClickable(visibility);

        correo.setFocusable(visibility);
        correo.setFocusableInTouchMode(visibility);
        correo.setClickable(visibility);

        telefono.setFocusable(visibility);
        telefono.setFocusableInTouchMode(visibility);
        telefono.setClickable(visibility);

        String edadString = edadPersonales.getText().toString();
        String[] arreglo = edadString.split(" ");
        edadPersonales.setText(arreglo[0]);
        edadPersonales.setFocusable(visibility);
        edadPersonales.setFocusableInTouchMode(visibility);
        edadPersonales.setClickable(visibility);

        if(visibility){
            nombre.setTextColor(getResources().getColor(R.color.primary_text));
            curp.setTextColor(getResources().getColor(R.color.primary_text));
            correo.setTextColor(getResources().getColor(R.color.primary_text));
            telefono.setTextColor(getResources().getColor(R.color.primary_text));
            edadPersonales.setTextColor(getResources().getColor(R.color.primary_text));

            sexo.setVisibility(View.INVISIBLE);

            String sexoString = sexo.getText().toString();
            if(sexoString.equals("Femenino")){
                radioF.setChecked(true);
            }else if(sexoString.equals("Masculino")){
                radioM.setChecked(true);
            }
            radios.setVisibility(View.VISIBLE);
        }else {
            sexo.setVisibility(View.VISIBLE);
            radios.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == DATOS_PERSONALES_LOADER){
            return  new CursorLoader(getContext(), MonitorECGContrato.PacienteEntry.CONTENT_URI,PacienteDAO.COLUMNS_PACIENTE_DATOS_PERSONALES,null,null,null);
        }else{
            return  null;
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor>  loader, Cursor data) {
        if(loader.getId() == DATOS_PERSONALES_LOADER){
            if(data.getCount() != 0) {
                data.moveToFirst();
                pacienteActualizar = new Paciente();
                pacienteActualizar.idPaciente = data.getInt(COLUMN_ID);

                String nombreString = data.getString(COLUMN_NOMBRE)+ " "+data.getString(COLUMN_APP)+" "+data.getString(COLUMN_APM);

                nombre.setText(nombreString);

                int edadInt = data.getInt(COLUMN_EDAD);
                edadPersonales.setText("No registrado");
                if(edadInt == 0){
                    edadPersonales.setText("No registrado");
                }else{
                    edadPersonales.setText(Integer.toString(edadInt) +" años");
                }
                String sexoString = data.getString(COLUMN_SEXO);
                if(sexoString.equals("F"))
                    sexo.setText("Femenino");
                else if(sexoString.equals("M"))
                    sexo.setText("Masculino");
                else
                    sexo.setText("No registrado");
                String curpString = data.getString(COLUMN_CURP);
                if(curpString.equals("")){
                    curp.setText("No registrado");
                }else{
                    curp.setText(curpString);
                }
                correo.setText(data.getString(COLUMN_CORREO));
                String telefonoString = data.getString(COLUMN_TELEFONO);
                if(telefonoString.equals("")){
                    telefono.setText("No registrado");
                }else{
                    telefono.setText(telefonoString);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }
}
