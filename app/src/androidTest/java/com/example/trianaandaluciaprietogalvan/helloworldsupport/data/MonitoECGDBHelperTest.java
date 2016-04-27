package com.example.trianaandaluciaprietogalvan.helloworldsupport.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.CardiologoEntry;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PacienteEntry;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PruebaEntry;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.ReporteEntry;

import java.util.HashSet;

/**
 * Created by trianaandaluciaprietogalvan on 02/04/16.
 */
public class MonitoECGDBHelperTest extends AndroidTestCase{

    private  SQLiteDatabase db;

    public void setUp(){
        deleteDatabase();
        db = new MonitorECGDBHelper(mContext).getWritableDatabase();
        assertTrue("La bd no esta abierta", db.isOpen());
    }

    @Override
    protected void tearDown() throws Exception {
        db.close();
    }

    public void testCreateDatabase(){
        HashSet<String> tablas = new HashSet<>();
        tablas.add(PacienteEntry.TABLE_NAME);

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);

        assertTrue("Verificando si existen tablas en la BD",cursor.moveToFirst());

        do {
            tablas.remove(cursor.getString(0));
        }while (cursor.moveToNext());
        cursor.close();

        assertTrue("No se crearon todas las tablas", tablas.isEmpty());

    }

    public void testCreateColumnsPaciente(){
        HashSet<String> columnas = new HashSet<>();
        columnas.add(PacienteEntry._ID);
        columnas.add(PacienteEntry.COLUMN_NOMBRE);
        columnas.add(PacienteEntry.COLUMN_APP);
        columnas.add(PacienteEntry.COLUMN_APM);
        columnas.add(PacienteEntry.COLUMN_SEXO);
        columnas.add(PacienteEntry.COLUMN_EDAD);
        columnas.add(PacienteEntry.COLUMN_CURP);
        columnas.add(PacienteEntry.COLUMN_CORREO);
        columnas.add(PacienteEntry.COLUMN_TELEFONO);
        columnas.add(PacienteEntry.COLUMN_PESO);
        columnas.add(PacienteEntry.COLUMN_PERSION_SISTOLICA);
        columnas.add(PacienteEntry.COLUMN_PERSION_DIASTOLICA);
        columnas.add(PacienteEntry.COLUMN_IMC);
        columnas.add(PacienteEntry.COLUMN_FRECUENCIA_RESPIRATORIA);
        columnas.add(PacienteEntry.COLUMN_ALTURA);
        columnas.add(PacienteEntry.COLUMN_FECHA_MODIFICACION);
        columnas.add(PacienteEntry.COLUMN_CARDIOLOGO_ID_CARDIOLOGO);

        Cursor cursor = db.rawQuery("PRAGMA table_info("+PacienteEntry.TABLE_NAME+")",null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex("name");
        do {
            String column = cursor.getString(columnIndex);
            columnas.remove(column);
        }while (cursor.moveToNext());

        assertTrue("No se crearon todas las columnas en la tabla paciente",columnas.isEmpty());
        cursor.close();
    }

    public void testCreateColumnsCardiologo(){
        HashSet<String> columnas = new HashSet<>();
        columnas.add(CardiologoEntry._ID);
        columnas.add(CardiologoEntry.COLUMN_NOMBRE);
        columnas.add(CardiologoEntry.COLUMN_APP);
        columnas.add(CardiologoEntry.COLUMN_APM);

        Cursor cursor = db.rawQuery("PRAGMA table_info("+CardiologoEntry.TABLE_NAME+")",null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex("name");
        do {
            String column = cursor.getString(columnIndex);
            columnas.remove(column);
        }while (cursor.moveToNext());

        assertTrue("No se crearon todas las columnas en la tabla cardiologo",columnas.isEmpty());
        cursor.close();
    }

    public void testCreateColumnsPrueba(){
        HashSet<String> columnas = new HashSet<>();
        columnas.add(PruebaEntry._ID);
        columnas.add(PruebaEntry.COLUMN_FECHA);
        columnas.add(PruebaEntry.COLUMN_HORA);
        columnas.add(PruebaEntry.COLUMN_MUESTRA_QRS);
        columnas.add(PruebaEntry.COLUMN_MUESTRA_COMPLETA);
        columnas.add(PruebaEntry.COLUMN_FRECUENCIA_CARDIACA);
        columnas.add(PruebaEntry.COLUMN_OBSERVACIONES);
        columnas.add(PruebaEntry.COLUMN_PACIENTE_ID_PACIENTE);
        columnas.add(PruebaEntry.COLUMN_REPORTE_ID_REPORTE);

        Cursor cursor = db.rawQuery("PRAGMA table_info("+PruebaEntry.TABLE_NAME+")",null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex("name");
        do {
            String column = cursor.getString(columnIndex);
            columnas.remove(column);
        }while (cursor.moveToNext());

        assertTrue("No se crearon todas las columnas en la tabla prueba",columnas.isEmpty());
        cursor.close();
    }

    public void testCreateColumnsReporte(){
        HashSet<String> columnas = new HashSet<>();
        columnas.add(ReporteEntry._ID);
        columnas.add(ReporteEntry.COLUMN_RECOMENDACIONES);
        columnas.add(ReporteEntry.COLUMN_ESTATUS);
        columnas.add(ReporteEntry.COLUMN_OBSERVACIONES);

        Cursor cursor = db.rawQuery("PRAGMA table_info("+ReporteEntry.TABLE_NAME+")",null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex("name");
        do {
            String column = cursor.getString(columnIndex);
            columnas.remove(column);
        }while (cursor.moveToNext());

        assertTrue("No se crearon todas las columnas en la tabla reporte",columnas.isEmpty());
        cursor.close();
    }

    private void deleteDatabase() {
        mContext.deleteDatabase(MonitorECGDBHelper.DATABASE_NAME);
    }
}
