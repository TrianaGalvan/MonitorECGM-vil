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
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_PERSION_ARTERIAL,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_IMC,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_ALTURA,
            MonitorECGContrato.PacienteEntry.TABLE_NAME+"."+MonitorECGContrato.PacienteEntry.COLUMN_PESO
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
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_PERSION_ARTERIAL,p.presionArterial);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_ALTURA,p.altura);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_PESO,p.peso);
        values.put(MonitorECGContrato.PacienteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO,p.cardiologo.idCardiologo);

        Uri insert = rs.insert(MonitorECGContrato.PacienteEntry.CONTENT_URI, values);
    }


}
