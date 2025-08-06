package com.easytable.app.AdaptersYClases.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.easytable.app.BasesDatos.DBHelper;
import android.database.Cursor;

import java.util.ArrayList;

public class HelperDAO {

    private final DBHelper dbHelper;

    public HelperDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void insertarConsulta(String nombre, String queja, String fecha) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NOMBRE, nombre);
        values.put(DBHelper.COLUMN_QUEJA, queja);
        values.put(DBHelper.COLUMN_FECHA, fecha);

        db.insert(DBHelper.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String> obtenerConsultas() {
        ArrayList<String> consultas = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, DBHelper.COLUMN_FECHA + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOMBRE));
                String queja = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_QUEJA));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_FECHA));

                String resultado = "📅 " + fecha + "\n👤 " + nombre + "\n📝 " + queja;
                consultas.add(resultado);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return consultas;
    }
}
