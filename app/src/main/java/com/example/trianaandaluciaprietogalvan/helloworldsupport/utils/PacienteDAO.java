package com.example.trianaandaluciaprietogalvan.helloworldsupport.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Paciente;

/**
 * Created by trianaandaluciaprietogalvan on 14/04/16.
 */
public class PacienteDAO {

    //PROYECCION DE PACIENTE
    public static final String[] PROYECION_PACIENTE_DEFAULT = new String[]{
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+ MonitorECGContrato.PacienteEntry._ID
    };

    //COLUMNAS PARA TODA LA PROYECCION DE PACIENTE
    public static final int COLUMN_NOMBRE_P = 0;
    public static final int COLUMN_APP_P = 1;
    public static final int COLUMN_APM_P = 2;
    public static final int COLUMN_CURP_P = 3;
    public static final int COLUMN_EDAD_P = 4;
    public static final int COLUMN_SEXO_P = 5;
    public static final int COLUMN_CORREO_P = 6;
    public static final int COLUMN_TELEFONO_P = 7;
    public static final int COLUMN_CONTRASENA_P = 8;
    public static final int COLUMN_FRECUENCIA_P = 9;
    public static final int COLUMN_PRESION_SIS_P = 10;
    public static final int COLUMN_PRESION_DIAS_P = 11;
    public static final int COLUMN_IMC_P = 12;
    public static final int COLUMN_ALTURA_P = 13;
    public static final int COLUMN_PESO_P = 14;
    public static final int COLUMN_ID_P = 15;
    public static final int COLUMN_CARDIOLOGO_ID_CARDIOLOGO = 16;
    public static final int COLUMN_FECHA_MODIFICACION = 17;


    public static String[] COLUMNS_PACIENTE = new String[]{
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_NOMBRE,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_APP,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_APM,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_CURP,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_EDAD,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_SEXO,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_CORREO,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_TELEFONO,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_CONTRASENA,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_FRECUENCIA_RESPIRATORIA,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_PERSION_SISTOLICA,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_PERSION_DIASTOLICA,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_IMC,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_ALTURA,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_PESO,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry._ID,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_FECHA_MODIFICACION
    };

    public static String[] COLUMNS_PACIENTE_DATOS_PERSONALES = new String[]{
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_NOMBRE,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_APP,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_APM,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_CURP,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_EDAD,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_SEXO,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_CORREO,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_TELEFONO,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry._ID,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_CONTRASENA
    };

    public static String[] COLUMNS_PACIENTE_DATOS_MEDICOS = new String[]{
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_FRECUENCIA_RESPIRATORIA,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_PERSION_SISTOLICA,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_PERSION_DIASTOLICA,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_IMC,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_ALTURA,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_PESO,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry._ID
    };



    public static void insertarPaciente(Paciente p , ContentResolver rs){
        ContentValues values = new ContentValues();

        values = new ContentValues();
        //guardar los datos en el content provider
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_NOMBRE,p.nombre);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_APP,p.apellidoPaterno);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_APM,p.apellidoMaterno);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_CURP,p.curp);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_EDAD,p.edad);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_CORREO,p.correo);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_TELEFONO,p.telefono);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_SEXO,p.sexo.toString());
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_CONTRASENA,p.contrasena);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_FRECUENCIA_RESPIRATORIA,p.frecuenciaRespiratoria);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_PERSION_DIASTOLICA,p.presionDiastolica);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_PERSION_SISTOLICA,p.presionSistolica);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_ALTURA,p.altura);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_PESO,p.peso);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO,p.cardiologo.idCardiologo);
        values.put(MonitorECGContrato.PacienteEntry._ID,p.idPaciente);

        Uri insert = rs.insert(MonitorECGContrato.PacienteEntry.CONTENT_URI, values);
    }


}
