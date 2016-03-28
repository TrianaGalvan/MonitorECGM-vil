package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private LinearLayout linearLayout;
    private ListView listView1;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.linear_prueba);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //opciones del menu
        MenuApp menu_data[] = new MenuApp[]
        {
                new MenuApp(R.drawable.folder, "Historial"),
                new MenuApp(R.drawable.monitor, "Enlazar electrocardiógrafo"),
                new MenuApp(R.drawable.senal_ecg, "Electrocardiograma"),
                new MenuApp(R.drawable.herramienta, "Herramientas"),
                new MenuApp(R.drawable.on_off, "Salir")
        };

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    fragmentoReemplazar = new AgregarElectrocardiografo();
                    titulo = "Agregar Electrocardiografo";
                    break;
                /* Electrocardiograma */
                case 2:
                    fragmentoReemplazar = new Electrocardiograma();
                    titulo = "Electrocardiograma";
                    break;
                /* Herramientas */
                case 3:
                    break;
                /* Salir */
                case 4:
                    Intent intentLogin = new Intent();
                    intentLogin.setClass(getApplicationContext(), LoginActivity.class);
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
