package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.AccountUtil;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.utils.MonitorECGUtils;


public class MainActivity extends AppCompatActivity {

    public static final String PARAM_CORREO = "correo";
    private DrawerLayout mDrawerLayout;
    private LinearLayout linearLayout;
    private ListView listView1;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String correo = getIntent().getStringExtra(PARAM_CORREO);

        linearLayout = (LinearLayout) findViewById(R.id.linear_prueba);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //opciones del menu
        MenuApp menu_data[] = new MenuApp[]
        {
                new MenuApp(R.drawable.folder, "Historial"),
                new MenuApp(R.drawable.monitor, "Enlazar electrocardiógrafo"),
                new MenuApp(R.drawable.senal_ecg, "Electrocardiograma"),
                new MenuApp(R.drawable.on_off, "Salir")
        };


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //colocar el correo
        Account cuenta = AccountUtil.getAccount(this);
        String name = cuenta.name;
        TextView textView = (TextView) findViewById(R.id.txtCorreoDrawer);
        textView.setText(name);

        listView1 = (ListView)findViewById(R.id.left_drawer);

        //crear el adapter para la lista del menu
        MenuAdapter adapter = new MenuAdapter(this,
                R.layout.listview_item_row, menu_data);

        listView1.setAdapter(adapter);

        // Evento para dar click en una opcion del menu
        listView1.setOnItemClickListener(new DrawerItemClickListener());

        //colocar el boton de menu
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mDrawerToggle.syncState();

        // Insert the fragment by replacing any existing fragment
        Historial historial = new Historial();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, historial)
                .commit();
        setTitle("Historial");
    }


    public class DrawerItemClickListener implements ListView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        /**
         * Swaps fragments in the main content view
         */
        private void selectItem(int position) {
            Fragment fragmentoReemplazar = null;
            String titulo = "";
            switch (position) {
                /* Historial */
                case 0:
                    fragmentoReemplazar = new Historial();
                    titulo = "Historial";
                    break;
                /* Agregar electrocardiografo */
                case 1:
                    Intent intentAgregarECG = new Intent(getBaseContext(),AgregarElectrocardiografo.class);
                    startActivity(intentAgregarECG);
                    break;
                /* Electrocardiograma */
                case 2:
                    Intent intentecg = new Intent(getBaseContext(),Grafica.class);
                    startActivity(intentecg);
                    break;
                /* Salir */
                case 3:
                    MonitorECGUtils.limpiarUltimoUsuarioEnSesion(getBaseContext());
                    AccountManager am = AccountManager.get(getBaseContext());
                    am.addAccount(getString(R.string.account_type), null, null, null,MainActivity.this, null, null);
                    limpiarBD();
                    finish();
                    break;
                default:
                    break;
            }

            if (fragmentoReemplazar != null) {
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragmentoReemplazar)
                        .commit();

                // Highlight the selected item, update the title, and close the drawer
                listView1.setItemChecked(position, true);
                setTitle(titulo);
                mDrawerLayout.closeDrawer(linearLayout);
            }
        }
    }

    public void limpiarBD(){
        //borrrar las pruebas del usuario en sesion
        ContentResolver rs = getContentResolver();
        Uri uriPrueba = MonitorECGContrato.PruebaEntry.CONTENT_URI;
        int rowsDeleted = rs.delete(uriPrueba, null, null);

        Uri uriReporte = MonitorECGContrato.ReporteEntry.CONTENT_URI;
        int rowsDeletedr = rs.delete(uriReporte,null,null);

        Uri uriCardiologo = MonitorECGContrato.CardiologoEntry.CONTENT_URI;
        int rowsDeleteCar = rs.delete(uriCardiologo,null,null);

        Uri uriPaciente = MonitorECGContrato.PacienteEntry.CONTENT_URI;
        int rowsPacienteCar = rs.delete(uriPaciente,null,null);

        Uri uriDispositivo = MonitorECGContrato.DispositivoEntry.CONTENT_URI;
        int rowsDeleteDisp = rs.delete(uriDispositivo,null,null);
    }

    public void onClickEmpezarGrafica(View view){
        Intent intentGrafica = new Intent();
        intentGrafica.setClass(this,Grafica.class);
        startActivity(intentGrafica);
    }

    public void onClickVerPerfil(View view){
        Fragment fragmentoReemplazar = null;
        String titulo = "";
        fragmentoReemplazar  = new Perfil();
        titulo = "Perfil";

        if (fragmentoReemplazar != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragmentoReemplazar)
                    .commit();

            setTitle(titulo);
            mDrawerLayout.closeDrawer(linearLayout);
        }

    }

    public void onClickVerElectrocardiograma(View view){
        Intent intent = new Intent();
        intent.setClass(this,Grafica.class);
        startActivity(intent);
    }
}
