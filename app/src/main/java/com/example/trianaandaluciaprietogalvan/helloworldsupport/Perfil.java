package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;


public class Perfil extends Fragment{

    TabLayout tab;
    FragmentActivity activity;
    ViewPager viewp;
    Adapter adapter;

    public Perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Insert the fragment by replacing any existing fragment
        DatosPersonales dp = new DatosPersonales();
        FragmentActivity acitivity = getActivity();
        FragmentManager fragmentManager = acitivity.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame_tabs, dp)
                .commit();

       return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity  = getActivity();
        tab = (TabLayout)activity.findViewById(R.id.tab_perfil);
        tab.addTab(tab.newTab().setText("Datos personales"),true);
        tab.addTab(tab.newTab().setText("Datos médicos"));

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                         @Override
                                         public void onTabSelected(TabLayout.Tab tab) {
                                             int position = tab.getPosition();
                                             Fragment fragmentoReemplazar = null;
                                             String tittle = "";
                                             switch (position) {
                                                 case 0:
                                                     fragmentoReemplazar = new DatosPersonales();
                                                     tittle = "Datos personales";
                                                     break;
                                                 case 1:
                                                     fragmentoReemplazar = new DatosMedicos();
                                                     tittle = "Datos médicos";
                                                     break;
                                                 default:
                                                     break;
                                             }

                                             if (fragmentoReemplazar != null) {
                                                 // Insert the fragment by replacing any existing fragment
                                                 FragmentManager fragmentManager = getFragmentManager();
                                                 fragmentManager.beginTransaction()
                                                         .replace(R.id.content_frame_tabs, fragmentoReemplazar)
                                                         .commit();
                                             }
                                         }

                                         @Override
                                         public void onTabUnselected(TabLayout.Tab tab) {

                                         }

                                         @Override
                                         public void onTabReselected(TabLayout.Tab tab) {

                                         }
                                     }

        );


    }



}
