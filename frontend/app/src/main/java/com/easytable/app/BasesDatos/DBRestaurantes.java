package com.easytable.app.BasesDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBRestaurantes extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurantes.db";
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla y columnas
    public static final String TABLE_RESTAURANTE = "Restaurante";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_IMAGEN_RESID = "imagen_res_id";
    public static final String COLUMN_RESERVA = "reserva";

    // SQL para crear la tabla
    private static final String CREATE_TABLE_RESTAURANTE =
            "CREATE TABLE " + TABLE_RESTAURANTE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE + " TEXT, " +
                    COLUMN_DESCRIPCION + " TEXT, " +
                    COLUMN_IMAGEN_RESID + " INTEGER, " +
                    COLUMN_RESERVA + " TEXT);";

    public DBRestaurantes(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RESTAURANTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTE);
        onCreate(db);
    }
}
