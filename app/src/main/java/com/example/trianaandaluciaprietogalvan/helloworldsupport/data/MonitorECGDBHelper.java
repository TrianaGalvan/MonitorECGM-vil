package com.example.trianaandaluciaprietogalvan.helloworldsupport.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PacienteEntry;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PruebaEntry;

import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.CardiologoEntry;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.ReporteEntry;

/**
 * Created by trianaandaluciaprietogalvan on 02/04/16.
 */
public class MonitorECGDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "MonitorECG.db";


    public MonitorECGDBHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PACIENTE_TABLE =
                "CREATE TABLE "+ PacienteEntry.TABLE_NAME +"("+
                        PacienteEntry._ID + " INTEGER PRIMARY KEY, "+
                        PacienteEntry.COLUMN_NOMBRE  + " TEXT NOT NULL, "+
                        PacienteEntry.COLUMN_APP  + " TEXT NOT NULL, "+
                        PacienteEntry.COLUMN_APM + " TEXT NOT NULL, "+
                        PacienteEntry.COLUMN_SEXO + " TEXT, "+
                        PacienteEntry.COLUMN_EDAD + " INTEGER, "+
                        PacienteEntry.COLUMN_CURP + " TEXT NOT NULL, "+
                        PacienteEntry.COLUMN_CORREO  + " TEXT NOT NULL, "+
                        PacienteEntry.COLUMN_TELEFONO  + " TEXT, "+
                        PacienteEntry.COLUMN_PESO  + " REAL, "+
                        PacienteEntry.COLUMN_PERSION_SISTOLICA  + " INTEGER, "+
                        PacienteEntry.COLUMN_PERSION_DIASTOLICA  + " INTEGER, "+
                        PacienteEntry.COLUMN_IMC  + " REAL, "+
                        PacienteEntry.COLUMN_FRECUENCIA_RESPIRATORIA  + " REAL, "+
                        PacienteEntry.COLUMN_ALTURA  + " REAL, " +
                        PacienteEntry.COLUMN_FECHA_MODIFICACION  + " TEXT, "+
                        PacienteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO+ " INTEGER NOT NULL, "+
                        PacienteEntry.COLUMN_CONTRASENA+ " TEXT NOT NULL, "+
                        PacienteEntry.BANDERA_ACTUALIZAR+ " INTEGER, "+
                        PacienteEntry.BANDERA_INSERTAR+ " INTEGER, "+
                        PacienteEntry.BANDERA_ELIMINAR+ " INTEGER, "+
                        //FOREIGN KEY a paciente
                        " FOREIGN KEY ("+PacienteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO+") REFERENCES "+
                        CardiologoEntry.TABLE_NAME + " ("+CardiologoEntry._ID+"), " +
                        " UNIQUE ("+PacienteEntry._ID+","+PacienteEntry.COLUMN_CORREO+") "+
                        "ON CONFLICT REPLACE"
                + ");";

        db.execSQL(SQL_CREATE_PACIENTE_TABLE);

        final String SQL_CREATE_PRUEBA_TABLE =
                "CREATE TABLE "+ PruebaEntry.TABLE_NAME +"("+
                        PruebaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        PruebaEntry.COLUMN_FECHA  + " TEXT NOT NULL, "+
                        PruebaEntry.COLUMN_HORA  + " TEXT NOT NULL, "+
                        PruebaEntry.COLUMN_MUESTRA_QRS + " TEXT, "+
                        PruebaEntry.COLUMN_MUESTRA_COMPLETA + " TEXT, "+
                        PruebaEntry.COLUMN_FRECUENCIA_CARDIACA + " INTEGER, "+
                        PruebaEntry.COLUMN_OBSERVACIONES + " TEXT, "+
                        PruebaEntry.COLUMN_FECHA_ENVIO  + " TEXT, "+
                        PruebaEntry.COLUMN_HORA_ENVIO  + " TEXT, "+
                        PruebaEntry.COLUMN_PACIENTE_ID_PACIENTE  + " INTEGER, "+
                        PruebaEntry.COLUMN_REPORTE_ID_REPORTE  + " INTEGER, "+

                        //FOREIGN KEY a paciente
                        " FOREIGN KEY ("+PruebaEntry.COLUMN_PACIENTE_ID_PACIENTE+") REFERENCES "+
                        PacienteEntry.TABLE_NAME + " ("+PacienteEntry._ID+"), " +
                        //FOREIGN KEY a reporte
                        " FOREIGN KEY ("+PruebaEntry.COLUMN_REPORTE_ID_REPORTE+") REFERENCES "+
                        ReporteEntry.TABLE_NAME + " ("+ReporteEntry._ID+"), " +
                        //Asegurar que una prueba tenga exacatmente un reporte
                        " UNIQUE ("+PruebaEntry._ID+","+PruebaEntry.COLUMN_REPORTE_ID_REPORTE+") "+
                        "ON CONFLICT REPLACE"
                        + ");";
        db.execSQL(SQL_CREATE_PRUEBA_TABLE);

        final String SQL_CREATE_REPORTE_TABLE =
                "CREATE TABLE "+ ReporteEntry.TABLE_NAME +"("+
                        ReporteEntry._ID + " INTEGER PRIMARY KEY, "+
                        ReporteEntry.COLUMN_OBSERVACIONES  + " TEXT, "+
                        ReporteEntry.COLUMN_RECOMENDACIONES + " TEXT, "+
                        ReporteEntry.COLUMN_ESTATUS + " INTEGER, "+
                        ReporteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO + " INTEGER );";
        db.execSQL(SQL_CREATE_REPORTE_TABLE);

        final String SQL_CREATE_CARDIOLOGO_TABLE =
                "CREATE TABLE "+ CardiologoEntry.TABLE_NAME +"("+
                        CardiologoEntry._ID + " INTEGER PRIMARY KEY, "+
                        CardiologoEntry.COLUMN_NOMBRE  + " TEXT NOT NULL, "+
                        CardiologoEntry.COLUMN_APP + " TEXT NOT NULL, "+
                        CardiologoEntry.COLUMN_APM + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_CARDIOLOGO_TABLE);

        final String SQL_CREATE_DISPOSITIVO_TABLE =
                "CREATE TABLE "+ MonitorECGContrato.DispositivoEntry.TABLE_NAME +"("+
                        MonitorECGContrato.DispositivoEntry.COLUMN_NOMBRE + " TEXT);";
        db.execSQL(SQL_CREATE_DISPOSITIVO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+PacienteEntry.TABLE_NAME);
        onCreate(db);
    }

}
