package com.easytable.app.BasesDatos;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.easytable.app.AdaptersYClases.DAO.UsuariosDao;
import com.easytable.app.AdaptersYClases.UsuariosGerentes;

@Database(entities = {UsuariosGerentes.class}, version = 2)
public abstract class DataBaseSQLUsuarios extends RoomDatabase {

    private static volatile DataBaseSQLUsuarios INSTANCE;

    public abstract UsuariosDao usuariosDao();

    // Crear una instancia de la base de datos
    public static DataBaseSQLUsuarios getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DataBaseSQLUsuarios.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DataBaseSQLUsuarios.class, "usuarios_db")
                            .fallbackToDestructiveMigration() // Cambiar la estrategia de migración si es necesario
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
