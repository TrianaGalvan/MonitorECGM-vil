package com.example.trianaandaluciaprietogalvan.helloworldsupport.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by trianaandaluciaprietogalvan on 02/04/16.
 */
public class MonitorECGContrato {
    public static final String CONTENT_AUTHORITY = "com.example.trianaandaluciaprietogalvan.helloworldsupport";

    public static final Uri BASE_CONTENT_AUTHORITY = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_PACIENTE = "paciente";
    public static final String PATH_PRUEBA = "prueba";
    public static final String PATH_REPORTE = "reporte";
    public static final String PATH_CARDIOLOGO = "cardiologo";


    public static final class PacienteEntry  implements BaseColumns{
        public static final String TABLE_NAME = "paciente";

        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_APP = "apellidoPaterno";
        public static final String COLUMN_APM= "apellidoMaterno";
        public static final String COLUMN_SEXO= "sexo";
        public static final String COLUMN_EDAD= "edad";
        public static final String COLUMN_CURP= "curp";
        public static final String COLUMN_CORREO = "correo";
        public static final String COLUMN_TELEFONO = "telefono";
        public static final String COLUMN_PESO = "peso";
        public static final String COLUMN_PERSION_ARTERIAL = "presionArterial";
        public static final String COLUMN_IMC = "imc";
        public static final String COLUMN_FRECUENCIA_RESPIRATORIA = "frecuenciaRespiratoria";
        public static final String COLUMN_ALTURA = "altura";
        public static final String COLUMN_FECHA_MODIFICACION = "fechaModificacion";
        public static final String COLUMN_CARDIOLOGO_ID_CARDIOLOGO = "idCardiologo";
        public static final String COLUMN_CONTRASENA = "contrasena";
        public static final String BANDERA_INSERTAR = "insertar";
        public static final String BANDERA_ACTUALIZAR = "actualizar";
        public static final String BANDERA_ELIMINAR = "eliminar";


        public static final Uri CONTENT_URI = BASE_CONTENT_AUTHORITY.buildUpon().appendPath(PATH_PACIENTE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_PACIENTE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_PACIENTE;

        public static Uri buildPacienteId(int id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static int getIdSettingFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

    }

    public static final class CardiologoEntry implements BaseColumns{
        public static final String TABLE_NAME = "cardiologo";

        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_APP = "apellidoPaterno";
        public static final String COLUMN_APM= "apellidoMaterno";

        public static final Uri CONTENT_URI = BASE_CONTENT_AUTHORITY.buildUpon().appendPath(PATH_CARDIOLOGO).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_CARDIOLOGO;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_CARDIOLOGO;

        public static Uri buildPacienteId(int id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static int getIdSettingFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

        public static Uri buildCardiologoId(int id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }



    public static final class PruebaEntry implements BaseColumns {
        public static final String TABLE_NAME = "prueba";

        public static final String COLUMN_FECHA = "fecha";
        public static final String COLUMN_HORA = "hora";
        public static final String COLUMN_MUESTRA_QRS = "muestraqrs";
        public static final String COLUMN_MUESTRA_COMPLETA = "muestracompleta";
        public static final String COLUMN_FRECUENCIA_CARDIACA = "frecuenciacardiaca";
        public static final String COLUMN_OBSERVACIONES = "observaciones";
        public static final String COLUMN_FECHA_ENVIO = "fechaenvio";
        public static final String COLUMN_HORA_ENVIO = "horaenvio";
        public static final String COLUMN_PACIENTE_ID_PACIENTE = "Paciente_idPaciente";
        public static final String COLUMN_REPORTE_ID_REPORTE = "Reporte_idReporte";

        public static final Uri CONTENT_URI = BASE_CONTENT_AUTHORITY.buildUpon().appendPath(PATH_PRUEBA).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_PRUEBA;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_PRUEBA;

        public static Uri buildPruebaId(int id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static int getIdSettingFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }

    public static final class ReporteEntry implements BaseColumns {
        public static final String TABLE_NAME = "reporte";

        public static final String COLUMN_RECOMENDACIONES = "recomendaciones";
        public static final String COLUMN_ESTATUS = "estatus";
        public static final String COLUMN_OBSERVACIONES = "observaciones";


        public static final Uri CONTENT_URI = BASE_CONTENT_AUTHORITY.buildUpon().appendPath(PATH_REPORTE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_REPORTE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_REPORTE;

        public static Uri buildReporteId(int id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static int getIdSettingFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }
}
