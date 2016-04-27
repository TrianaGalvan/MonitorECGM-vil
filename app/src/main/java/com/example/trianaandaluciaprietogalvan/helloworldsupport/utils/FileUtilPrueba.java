package com.example.trianaandaluciaprietogalvan.helloworldsupport.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by trianaandaluciaprietogalvan on 25/04/16.
 */
public class FileUtilPrueba {
    public static String NOMBRE_ARCHIVO = null;

    public static OutputStreamWriter generarArchivio(String nombre){
        OutputStreamWriter osw = null;
        File pruebaFile;
        //generar el nombre del archivo
        //verificar si hay memoria externa
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        pruebaFile = new File(Environment.getExternalStorageDirectory(),nombre);
        NOMBRE_ARCHIVO = pruebaFile.getAbsolutePath();
        try {
            pruebaFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(pruebaFile.exists())
        {
            try {
                osw =  new OutputStreamWriter(new FileOutputStream(pruebaFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.i("FileUtil","file created: "+pruebaFile);
        }
        return osw;
    }

    public static String generarNombreArch(Context contexto){
        String dateHour = HourUtils.getDateHourSinPuntos();
        String[] vals  = dateHour.split(" ");

        ContentResolver rs = contexto.getContentResolver();
        String[] proyeccion = new String[]{
                MonitorECGContrato.PacienteEntry._ID
        };

        int random = (int) (Math.random() * ( 100 - 0 ));

        Cursor cursor = rs.query(MonitorECGContrato.PacienteEntry.CONTENT_URI, proyeccion, null, null, null);
        cursor.moveToFirst();
        int idPaciente = cursor.getInt(0);

        return dateHour+"_"+random+"_"+idPaciente+".txt";
    }
}
