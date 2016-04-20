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
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.adapters.SpinnerCardiologosAdapter;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContentProvider;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Cardiologo;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.AccountUtil;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.CardiologoDAO;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.PacienteDAO;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.web.ServicioWeb;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DatosMedicos extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Callback<List<Cardiologo>> {

    @Bind(R.id.txtFrecuenciaRespDM)
    TextView fecuenciaResp;
    @Bind(R.id.txtPresionSisDM)
    EditText presioSis;
    @Bind(R.id.txtPresionDiasDM)
    EditText presioDias;
    @Bind(R.id.txtPesoDM)
    EditText peso;
    @Bind(R.id.txtAlturaDM)
    EditText altura;
    @Bind(R.id.txtImcDM)
    EditText imc;
    @Bind(R.id.modificarCardiologo)
    Spinner cardiologos;
    @Bind(R.id.fab_editar)
    FloatingActionButton fab_editar;
    @Bind(R.id.fab_ok)
    FloatingActionButton fab_ok;
    @Bind(R.id.txtCardiologo)
    EditText cardiologo;

    //COLUMNAS DE LAS PROYECCIONES
    public static final int COLUMN_FRECUENCIA = 0;
    public static final int COLUMN_PRESION_SISTOLICA = 1;
    public static final int COLUMN_PRESION_DIASTOLICA = 2;
    public static final int COLUMN_IMC = 3;
    public static final int COLUMN_ALTURA = 4;
    public static final int COLUMN_PESO = 5;
    public static final int COLUMN_CARDIOLOGO_ID_CARDIOLOGO = 6;
    public static final int COLUMN_ID_PACIENTE = 7;

    //COLUMNAS DE LAS PROYECCIONES DE CARDIOLOGO
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NOMBRE = 1;
    public static final int COLUMN_APP = 2;
    public static final int COLUMN_APM = 3;

    public int idPaciente = 0;


    //SPINNNER ADAPTER
    SpinnerCardiologosAdapter sca = null;

    //BUNDLE LOADER
    public static final String BUNDLE_ID_CARDIOLOGO = "idCardiologo";


    //SELECCION DE CARDIOLOGO
    public static final String SELECCION_PACIENTE = MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+
                                                    MonitorECGContrato.PacienteEntry._ID +"= ?";

    public static final int LOADER_DATOS_MEDICOS = 10;
    public static final int LOADER_CARDIOLOGO = 20;



    public DatosMedicos() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_datos_medicos, container, false);
        ButterKnife.bind(this, view);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_DATOS_MEDICOS, null, this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FrameLayout view = (FrameLayout)getView();
        fab_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_ok.setVisibility(View.VISIBLE);
                fab_editar.setVisibility(View.INVISIBLE);
                setEditable(true);
            }
        });

        fab_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_ok.setVisibility(View.INVISIBLE);
                fab_editar.setVisibility(View.VISIBLE);
                setEditable(false);
                actualizarDatosMedicos(AccountUtil.getAccount(getContext()));
            }
        });
    }

    private void actualizarDatosMedicos(Account account) {
        ContentResolver rs = getContext().getContentResolver();

        ContentValues cv = new ContentValues();

        if(fecuenciaResp.getText().toString().isEmpty()){
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_FRECUENCIA_RESPIRATORIA,0);
        }else{
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_FRECUENCIA_RESPIRATORIA,Integer.parseInt(fecuenciaResp.getText().toString()));
        }

        if(presioSis.getText().toString().isEmpty()){
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_PERSION_SISTOLICA,0);
        }else{
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_PERSION_SISTOLICA,Integer.parseInt(presioSis.getText().toString()));
        }

        if(presioDias.getText().toString().isEmpty()){
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_PERSION_DIASTOLICA,0);
        }else{
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_PERSION_DIASTOLICA,Integer.parseInt(presioDias.getText().toString()));
        }

        if(peso.getText().toString().isEmpty()){
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_PESO,0);
        }else{
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_PESO,Integer.parseInt(peso.getText().toString()));
        }

        if(altura.getText().toString().isEmpty()){
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_ALTURA,0);
        }else{
            cv.put(MonitorECGContrato.PacienteEntry.COLUMN_ALTURA,Float.parseFloat(altura.getText().toString()));
        }

        //ACTUALIZAR EL IMC
        int pesoImc = Integer.parseInt(peso.getText().toString());
        float alturaImc = Float.parseFloat(altura.getText().toString());
        float imcCaclc = 0;

        if(pesoImc != 0 && alturaImc != 0){
            imcCaclc = (float) (pesoImc / Math.pow(alturaImc,2));
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        cv.put(MonitorECGContrato.PacienteEntry.COLUMN_IMC,df.format(imcCaclc));

        //ACTUALIZAR EL CARDIOLOGO
        final Cardiologo car  = (Cardiologo) cardiologos.getSelectedItem();

        //verificar si no existe el cardiologo
        verificarRegistroMedico(car);

        cv.put(MonitorECGContrato.PacienteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO,car.idCardiologo);

        //ACTUALIZAR LA LLAVE FORANEA DE PACIENTE A CARDIOLOGO
        String[] args = new String[]{
                Integer.toString(idPaciente)
        };

        //ACTUALIZAR LA FECHA DE MODIFICACION
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        cv.put(MonitorECGContrato.PacienteEntry.COLUMN_FECHA_MODIFICACION,strDate);

        //ACTIVAR LA BANDERA DE ACTUALIZAR
        cv.put(MonitorECGContrato.PacienteEntry.BANDERA_ACTUALIZAR,1);

        Uri.Builder builder = MonitorECGContrato.PacienteEntry.CONTENT_URI.buildUpon();
        Uri sync_update_uri = builder.appendQueryParameter(MonitorECGContentProvider.QUERY_SYNC, "true").build();

        rs.update(sync_update_uri,cv,SELECCION_PACIENTE,args);

    }

    public void verificarRegistroMedico(final Cardiologo car){
        final ContentResolver rs = getContext().getContentResolver();
        Uri uriCardiologoId = MonitorECGContrato.CardiologoEntry.buildCardiologoId(car.idCardiologo);
        Cursor cursor = rs.query(uriCardiologoId, CardiologoDAO.PROYECCION_VERIFICAR_CARDIOLOGO, null, null, null);
        //verificar si el cursor tiene datos
        int count = cursor.getCount();
        //no existe el cardiologo regstrado en la bd
        if(count == 0){
            CardiologoDAO.insertarCardiologo(car, rs);
        }
    }

    public void setEditable(boolean visibility){
        if(visibility){
            if(!fecuenciaResp.getText().toString().equals("No registrado")){
                String[] array = fecuenciaResp.getText().toString().split(" ");
                fecuenciaResp.setText(array[0]);
            }else{
                fecuenciaResp.setText("");
            }
        }
        fecuenciaResp.setEnabled(visibility);

        if(visibility){
            if(presioSis.getText().toString().equals("No registrado")){
                presioSis.setText("");
            }
        }
        presioSis.setEnabled(visibility);

        if(visibility){
            if(presioDias.getText().toString().equals("No registrado")){
                presioDias.setText("");
            }
        }
        presioDias.setEnabled(visibility);

        if(visibility){
            if(!peso.getText().toString().equals("No registrado")){
                String[] array = peso.getText().toString().split(" ");
                peso.setText(array[0]);
            }else {
                peso.setText("");
            }
        }
        peso.setEnabled(visibility);

        if(visibility){
            if(!altura.getText().toString().equals("No registrado")) {
                String[] array = altura.getText().toString().split(" ");
                altura.setText(array[0]);
            }else{
                altura.setText("");
            }
        }
        altura.setEnabled(visibility);

        if(visibility){
            //crear el adaptador
            sca = new SpinnerCardiologosAdapter(getContext());
            //asignar el adaptador al spinner
            cardiologos.setAdapter(sca);
            //obtener los cardiologos para colocarlos en el spinner
            ServicioWeb.obtenerCardiologos(this);

            cardiologos.setVisibility(View.VISIBLE);
            cardiologo.setVisibility(View.INVISIBLE);
        }else{
            cardiologos.setVisibility(View.INVISIBLE);
            cardiologo.setVisibility(View.VISIBLE);
            cardiologo.setEnabled(visibility);
        }
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == LOADER_DATOS_MEDICOS){
            return  new CursorLoader(getContext(), MonitorECGContrato.PacienteEntry.CONTENT_URI, PacienteDAO.COLUMNS_PACIENTE_DATOS_MEDICOS,null,null,null);
        }else if(id == LOADER_CARDIOLOGO){
            int idCar = args.getInt(BUNDLE_ID_CARDIOLOGO);
            return  new CursorLoader(getContext(), MonitorECGContrato.CardiologoEntry.buildCardiologoId(idCar), CardiologoDAO.PROYECCION_CARDIOLOGO,null,null,null);
        }
        else {
            return  null;
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LOADER_DATOS_MEDICOS) {
            if (data.getCount() != 0) {
                data.moveToFirst();
                int f = data.getInt(COLUMN_FRECUENCIA);
                if(f == 0){
                    fecuenciaResp.setText("No registrado");
                }else{
                    fecuenciaResp.setText(Integer.toString(f)+" respiraciones por minuto");
                }
                int ps = data.getInt(COLUMN_PRESION_SISTOLICA);
                int pd = data.getInt(COLUMN_PRESION_DIASTOLICA);
                if(ps != 0 && pd != 0){
                    presioSis.setText(Integer.toString(ps));
                    presioDias.setText(Integer.toString(pd));
                }else{
                    presioSis.setText("No registrado");
                    presioDias.setText("No registrado");
                }
                int pe = data.getInt(COLUMN_PESO);
                if(pe == 0){
                    peso.setText("No registrado");
                }else{
                    peso.setText(Integer.toString(pe)+" Kg");
                }
                float i = data.getFloat(COLUMN_IMC);
                if(i == 0){
                    imc.setText("No registrado");
                }else{
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    imc.setText(df.format(i));
                }
                float a = data.getFloat(COLUMN_ALTURA);
                if(a == 0){
                    altura.setText("No registrado");
                }else{
                    altura.setText(Float.toString(a)+" m");
                }

                //obtener el id del paciente
                idPaciente = data.getInt(COLUMN_ID_PACIENTE);

                int id = data.getInt(COLUMN_CARDIOLOGO_ID_CARDIOLOGO);
                Bundle bundle = new Bundle();
                bundle.putInt(BUNDLE_ID_CARDIOLOGO,id);

                LoaderManager loaderManager = getLoaderManager();
                android.support.v4.content.Loader<Cursor> loaderCar = loaderManager.getLoader(LOADER_CARDIOLOGO);
                if(loaderCar == null){
                    loaderManager.initLoader(LOADER_CARDIOLOGO, bundle, this);
                }else{
                    loaderManager.restartLoader(LOADER_CARDIOLOGO,bundle,this);
                }


            }
        }
        else if(loader.getId() == LOADER_CARDIOLOGO) {
            data.moveToFirst();
            String nombreCar = data.getString(COLUMN_NOMBRE) + " "+data.getString(COLUMN_APP)+" "+data.getString(COLUMN_APM);
            cardiologo.setText(nombreCar);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    @Override
    public void onResponse(Call<List<Cardiologo>> call, Response<List<Cardiologo>> response) {
        if(response.isSuccessful()){
            List<Cardiologo> lista = response.body();
            sca.setListaCardiologos(lista);
        }
    }

    @Override
    public void onFailure(Call<List<Cardiologo>> call, Throwable t) {

    }


}
