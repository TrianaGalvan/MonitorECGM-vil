package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class Perfil extends Fragment{

    @Bind(R.id.viewPager)
    ViewPager viewpager;
    @Bind(R.id.tab_perfil)
    TabLayout tab;

    FragmentActivity activity;

    public Perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        ButterKnife.bind(this, view);

       return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TabsAdapter adapter = new TabsAdapter(getChildFragmentManager());

        adapter.addFragment(new DatosPersonales(),"Datos personales");
        adapter.addFragment(new DatosMedicos(), "Datos médicos");

        viewpager.setAdapter(adapter);
        tab.setupWithViewPager(viewpager);

    }

    public class TabsAdapter extends FragmentPagerAdapter{
        public static final int NUM_TABS = 2;
        private final List<Fragment> fragmentos = new ArrayList<>(NUM_TABS);
        private final List<String> fragmentos_titulos = new ArrayList<>(NUM_TABS);

        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentos.get(position);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentos_titulos.get(position);
        }

        public void addFragment(Fragment fragmento , String tituloFragmento){
            fragmentos.add(fragmento);
            fragmentos_titulos.add(tituloFragmento);
        }
    }

}
