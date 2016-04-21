package com.example.trianaandaluciaprietogalvan.helloworldsupport.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.*;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.CONTENT_AUTHORITY;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.CardiologoEntry;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PATH_CARDIOLOGO;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PATH_DISPOSITIVO;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PATH_PACIENTE;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PATH_PRUEBA;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PATH_REPORTE;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PacienteEntry;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.PruebaEntry;
import static com.example.trianaandaluciaprietogalvan.helloworldsupport.data.MonitorECGContrato.ReporteEntry;

public class MonitorECGContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MonitorECGDBHelper mOpenHelper;

    //QUERY PARAMETERS
    public static final String QUERY_SYNC = "sync";

    static final int PACIENTE_WITH_ID = 100;
    static final int PACIENTE = 101;
    static final int CARDIOLOGO = 200;
    static final int CARDIOLOGO_WITH_ID = 201;
    static final int PRUEBA = 300;
    static final int PRUEBA_WITH_ID =301;
    static final int REPORTE = 400;
    static final int REPORTE_WITH_ID = 401;
    static final int DISPOSITIVO = 500;

    //paciente.idPaciente= ?
    private static final String sPacienteSettingId =
            PacienteEntry.TABLE_NAME +
                    "." + PacienteEntry._ID + " = ?";

    //cardiologo.idCardiologo= ?
    private static final String sCardiologoSettingId =
            CardiologoEntry.TABLE_NAME +
                    "." + CardiologoEntry._ID + " = ?";

    //prueba.idPrueba= ?
    private static final String sPruebaSettingId =
            PruebaEntry.TABLE_NAME +
                    "." + PruebaEntry._ID + " = ?";

    //reporte.idReporte= ?
    private static final String sReporteSettingId =
            ReporteEntry.TABLE_NAME +
                    "." + ReporteEntry._ID + " = ?";


    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PACIENTE,PACIENTE);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PACIENTE + "/#",PACIENTE_WITH_ID);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_CARDIOLOGO,CARDIOLOGO);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_CARDIOLOGO + "/#",CARDIOLOGO_WITH_ID);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PRUEBA,PRUEBA);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PRUEBA + "/#",PRUEBA_WITH_ID);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_REPORTE,REPORTE);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_REPORTE + "/#",REPORTE_WITH_ID);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_DISPOSITIVO,DISPOSITIVO);
        return uriMatcher;
    }

    public MonitorECGContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if(selection == null){
            selection = "1";
        }

        switch (match) {
            case PACIENTE:
                rowsDeleted = db.delete(PacienteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CARDIOLOGO:
                rowsDeleted = db.delete(CardiologoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRUEBA:
                rowsDeleted = db.delete(PruebaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REPORTE:
                rowsDeleted = db.delete(ReporteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DISPOSITIVO:
                rowsDeleted = db.delete(DispositivoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case PACIENTE:
                return PacienteEntry.CONTENT_TYPE;
            case PACIENTE_WITH_ID:
                return PacienteEntry.CONTENT_ITEM_TYPE;
            case CARDIOLOGO:
                return CardiologoEntry.CONTENT_ITEM_TYPE;
            case CARDIOLOGO_WITH_ID:
                return CardiologoEntry.CONTENT_TYPE;
            case PRUEBA:
                return PruebaEntry.CONTENT_TYPE;
            case DISPOSITIVO:
                return DispositivoEntry.CONTENT_TYPE;
            case PRUEBA_WITH_ID:
                return PruebaEntry.CONTENT_ITEM_TYPE;
            case REPORTE:
                return ReporteEntry.CONTENT_TYPE;
            case REPORTE_WITH_ID:
                return ReporteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case PACIENTE: {
                long _id = db.insert(PacienteEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = PacienteEntry.buildPacienteId((int) _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case PRUEBA: {
                long _id = db.insert(PruebaEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = PruebaEntry.buildPruebaId((int) _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case CARDIOLOGO: {
                long _id = db.insert(CardiologoEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = CardiologoEntry.buildCardiologoId((int) _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case DISPOSITIVO: {
                long _id = db.insert(DispositivoEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DispositivoEntry.buildDispositivoId((int) _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case REPORTE:
                long _id = db.insert(ReporteEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ReporteEntry.buildReporteId((int) _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null,false);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MonitorECGDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "paciente/#"
            case PACIENTE_WITH_ID:
            {
                retCursor = getPacienteById(uri, projection, sortOrder);
                break;
            }
            case DISPOSITIVO:
            {
                retCursor = getDispositivo(projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "paciente"
            case PACIENTE: {
                retCursor = getPaciente(projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "cardiologo/#"
            case CARDIOLOGO_WITH_ID:
            {
                retCursor = getCardiologoById(uri, projection, sortOrder);
                break;
            }
            // "paciente"
            case CARDIOLOGO: {
                retCursor = getCardiologo(projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "prueba/#"
            case PRUEBA_WITH_ID:
            {
                retCursor = getPruebaById(uri, projection, sortOrder);
                break;
            }
            // "prueba"
            case PRUEBA: {
                retCursor = getPrueba(projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "reporte/#"
            case REPORTE_WITH_ID:
            {
                retCursor = getReporteById(uri, projection, sortOrder);
                break;
            }
            // "reporte"
            case REPORTE: {
                retCursor = getReporte(projection, selection, selectionArgs, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getReporte(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        return db.query(ReporteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getReporteById(Uri uri, String[] projection, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int id = ReporteEntry.getIdSettingFromUri(uri);

        return db.query(ReporteEntry.TABLE_NAME,
                projection,
                sReporteSettingId,
                new String[]{Integer.toString(id)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getPrueba(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String join_prueba_reporte = PruebaEntry.TABLE_NAME + " INNER JOIN "+
                                    ReporteEntry.TABLE_NAME+" ON "+
                                    PruebaEntry.TABLE_NAME +"."+PruebaEntry.COLUMN_REPORTE_ID_REPORTE+" = "+
                                    ReporteEntry.TABLE_NAME+"."+ReporteEntry._ID;

        return db.query(join_prueba_reporte,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getPruebaById(Uri uri, String[] projection, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int id = PruebaEntry.getIdSettingFromUri(uri);

        String join_prueba_reporte = PruebaEntry.TABLE_NAME + " INNER JOIN "+
                ReporteEntry.TABLE_NAME+" ON "+
                PruebaEntry.TABLE_NAME +"."+PruebaEntry.COLUMN_REPORTE_ID_REPORTE+" = "+
                ReporteEntry.TABLE_NAME+"."+ReporteEntry._ID;

        return db.query(join_prueba_reporte,
                projection,
                sPruebaSettingId,
                new String[]{Integer.toString(id)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCardiologo(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        return db.query(CardiologoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getCardiologoById(Uri uri, String[] projection, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int id = CardiologoEntry.getIdSettingFromUri(uri);

        return db.query(CardiologoEntry.TABLE_NAME,
                projection,
                sCardiologoSettingId,
                new String[]{Integer.toString(id)},
                null,
                null,
                sortOrder
        );

    }

    private Cursor getPaciente(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        return db.query(PacienteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getDispositivo(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        return db.query(DispositivoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getPacienteById(Uri uri, String[] projection, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int id = PacienteEntry.getIdSettingFromUri(uri);

        return db.query(PacienteEntry.TABLE_NAME,
                projection,
                sPacienteSettingId,
                new String[]{Integer.toString(id)},
                null,
                null,
                sortOrder
        );

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case PACIENTE:
                rowsUpdated = db.update(PacienteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PRUEBA:
                rowsUpdated = db.update(PruebaEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CARDIOLOGO:
                rowsUpdated = db.update(CardiologoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REPORTE:
                rowsUpdated = db.update(ReporteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        if(rowsUpdated != 0) {
            String sync = uri.getQueryParameter(QUERY_SYNC);

            if(sync != null && sync.equals("true")){
                getContext().getContentResolver().notifyChange(uri, null, true);
            }else{
                getContext().getContentResolver().notifyChange(uri, null, false);
            }

        }

        return rowsUpdated;
    }

}
