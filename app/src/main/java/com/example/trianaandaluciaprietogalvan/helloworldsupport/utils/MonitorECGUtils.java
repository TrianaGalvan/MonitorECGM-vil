package com.example.trianaandaluciaprietogalvan.helloworldsupport.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by trianaandaluciaprietogalvan on 03/04/16.
 */
public class MonitorECGUtils {
    public static String obtenerUltimoUsuarioEnSesion(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getString("correo",null);
    }

    public static void guardarUltimoUsuarioEnSesion(Context c, String correo) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("correo", correo);
        editor.apply();
    }

    public static void limpiarUltimoUsuarioEnSesion(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("correo");
        editor.apply();
    }
}
