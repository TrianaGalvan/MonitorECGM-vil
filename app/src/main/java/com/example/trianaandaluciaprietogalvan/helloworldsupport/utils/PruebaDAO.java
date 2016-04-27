package com.example.trianaandaluciaprietogalvan.helloworldsupport.utils;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;

/**
 * Created by trianaandaluciaprietogalvan on 24/04/16.
 */
public class PruebaDAO {

    public static final int COLUMN_PRUEBA_COMPLETA_ID = 0;
    public static final int COLUMN_PRUEBA_COMPLETA_FECHA = 1;
    public static final int COLUMN_PRUEBA_COMPLETA_HORA = 2;
    public static final int COLUMN_PRUEBA_COMPLETA_MUESTRA_QRS = 3;
    public static final int COLUMN_PRUEBA_COMPLETA_MUESTRA_COMPLETO = 4;
    public static final int COLUMN_PRUEBA_COMPLETA_FRECUENCIA = 5;
    public static final int COLUMN_PRUEBA_COMPLETA_OBSERVACIONES = 6;
    public static final int COLUMN_PRUEBA_COMPLETA_PACIENTE_ID_PACIENTE = 7;

    public static final String[] PROYECCION_PRUEBA_COMPLETA = new String[]{
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry._ID,
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry.COLUMN_FECHA,
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry.COLUMN_HORA,
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry.COLUMN_MUESTRA_QRS,
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry.COLUMN_MUESTRA_COMPLETA,
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry.COLUMN_FRECUENCIA_CARDIACA,
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry.COLUMN_OBSERVACIONES,
            MonitorECGContrato.PruebaEntry.TABLE_NAME+"."+MonitorECGContrato.PruebaEntry.COLUMN_PACIENTE_ID_PACIENTE
    };

}
