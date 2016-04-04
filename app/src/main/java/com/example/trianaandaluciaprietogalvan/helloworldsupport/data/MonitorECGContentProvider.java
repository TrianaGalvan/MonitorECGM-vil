package com.example.trianaandaluciaprietogalvan.helloworldsupport.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MonitorECGContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MonitorECGDBHelper mOpenHelper;

    static final int PACIENTE_WITH_ID = 100;
    static final int PACIENTE = 101;
    static final int CARDIOLOGO = 200;
    static final int CARDIOLOGO_WITH_ID = 201;
    static final int PRUEBA = 300;
    static final int PRUEBA_WITH_ID =301;
    static final int REPORTE = 400;
    static final int REPORTE_WITH_ID = 401;


    //paciente.idPaciente= ?
    private static final String sPacienteSettingId =
            MonitorECGContrato.PacienteEntry.TABLE_NAME +
                    "." + MonitorECGContrato.PacienteEntry._ID + " = ?";

    //cardiologo.idCardiologo= ?
    private static final String sCardiologoSettingId =
            MonitorECGContrato.CardiologoEntry.TABLE_NAME +
                    "." + MonitorECGContrato.CardiologoEntry._ID + " = ?";

    //prueba.idPrueba= ?
    private static final String sPruebaSettingId =
            MonitorECGContrato.PruebaEntry.TABLE_NAME +
                    "." + MonitorECGContrato.PruebaEntry._ID + " = ?";

    //reporte.idReporte= ?
    private static final String sReporteSettingId =
            MonitorECGContrato.ReporteEntry.TABLE_NAME +
                    "." + MonitorECGContrato.ReporteEntry._ID + " = ?";


    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        uriMatcher.addURI(MonitorECGContrato.CONTENT_AUTHORITY,MonitorECGContrato.PATH_PACIENTE,PACIENTE);
        uriMatcher.addURI(MonitorECGContrato.CONTENT_AUTHORITY,MonitorECGContrato.PATH_PACIENTE + "/#",PACIENTE_WITH_ID);

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
                rowsDeleted = db.delete(MonitorECGContrato.PacienteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CARDIOLOGO:
                rowsDeleted = db.delete(MonitorECGContrato.CardiologoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRUEBA:
                rowsDeleted = db.delete(MonitorECGContrato.PruebaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REPORTE:
                rowsDeleted = db.delete(MonitorECGContrato.ReporteEntry.TABLE_NAME, selection, selectionArgs);
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
                return MonitorECGContrato.PacienteEntry.CONTENT_TYPE;
            case PACIENTE_WITH_ID:
                return MonitorECGContrato.PacienteEntry.CONTENT_ITEM_TYPE;
            case CARDIOLOGO:
                return MonitorECGContrato.CardiologoEntry.CONTENT_ITEM_TYPE;
            case CARDIOLOGO_WITH_ID:
                return MonitorECGContrato.CardiologoEntry.CONTENT_TYPE;
            case PRUEBA:
                return MonitorECGContrato.PruebaEntry.CONTENT_TYPE;
            case PRUEBA_WITH_ID:
                return MonitorECGContrato.PruebaEntry.CONTENT_ITEM_TYPE;
            case REPORTE:
                return MonitorECGContrato.ReporteEntry.CONTENT_TYPE;
            case REPORTE_WITH_ID:
                return MonitorECGContrato.ReporteEntry.CONTENT_ITEM_TYPE;
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
                long _id = db.insert(MonitorECGContrato.PacienteEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MonitorECGContrato.PacienteEntry.buildPacienteId((int) _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case PRUEBA: {
                long _id = db.insert(MonitorECGContrato.PruebaEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MonitorECGContrato.PruebaEntry.buildPruebaId((int) _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
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

        return db.query(MonitorECGContrato.ReporteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getReporteById(Uri uri, String[] projection, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int id = MonitorECGContrato.ReporteEntry.getIdSettingFromUri(uri);

        return db.query(MonitorECGContrato.ReporteEntry.TABLE_NAME,
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

        return db.query(MonitorECGContrato.PruebaEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getPruebaById(Uri uri, String[] projection, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int id = MonitorECGContrato.PruebaEntry.getIdSettingFromUri(uri);

        return db.query(MonitorECGContrato.PruebaEntry.TABLE_NAME,
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

        return db.query(MonitorECGContrato.CardiologoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getCardiologoById(Uri uri, String[] projection, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int id = MonitorECGContrato.CardiologoEntry.getIdSettingFromUri(uri);

        return db.query(MonitorECGContrato.CardiologoEntry.TABLE_NAME,
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

        return db.query(MonitorECGContrato.PacienteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getPacienteById(Uri uri, String[] projection, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int id = MonitorECGContrato.PacienteEntry.getIdSettingFromUri(uri);

        return db.query(MonitorECGContrato.PacienteEntry.TABLE_NAME,
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
                rowsUpdated = db.update(MonitorECGContrato.PacienteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PRUEBA:
                rowsUpdated = db.update(MonitorECGContrato.PruebaEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CARDIOLOGO:
                rowsUpdated = db.update(MonitorECGContrato.CardiologoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REPORTE:
                rowsUpdated = db.update(MonitorECGContrato.ReporteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        if(rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
