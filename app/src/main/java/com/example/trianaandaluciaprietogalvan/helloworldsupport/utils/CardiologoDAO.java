package com.example.trianaandaluciaprietogalvan.helloworldsupport.utils;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Cardiologo;

/**
 * Created by trianaandaluciaprietogalvan on 14/04/16.
 */
public class CardiologoDAO {

    public static final String[] PROYECCION_VERIFICAR_CARDIOLOGO = new String[]{
            MonitorECGContrato.CardiologoEntry.TABLE_NAME+"."+ MonitorECGContrato.CardiologoEntry._ID
    };

    public static final String[] PROYECCION_CARDIOLOGO = new String[]{
            MonitorECGContrato.CardiologoEntry.TABLE_NAME+"."+ MonitorECGContrato.CardiologoEntry._ID,
            MonitorECGContrato.CardiologoEntry.TABLE_NAME+"."+ MonitorECGContrato.CardiologoEntry.COLUMN_NOMBRE,
            MonitorECGContrato.CardiologoEntry.TABLE_NAME+"."+ MonitorECGContrato.CardiologoEntry.COLUMN_APP,
            MonitorECGContrato.CardiologoEntry.TABLE_NAME+"."+ MonitorECGContrato.CardiologoEntry.COLUMN_APM
    };

    public static void insertarCardiologo(Cardiologo car,ContentResolver rs){
        ContentValues values = new ContentValues();
        //guardar el cardiologo
        values.put(MonitorECGContrato.CardiologoEntry._ID,car.idCardiologo);
        values.put(MonitorECGContrato.CardiologoEntry.COLUMN_NOMBRE,car.nombre);
        values.put(MonitorECGContrato.CardiologoEntry.COLUMN_APP,car.apellidoPaterno);
        values.put(MonitorECGContrato.CardiologoEntry.COLUMN_APM, car.apellidoMaterno);

        rs.insert(MonitorECGContrato.CardiologoEntry.CONTENT_URI, values);
    }

}
