package com.easytable.app.AdaptersYClases.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.easytable.app.AdaptersYClases.Restaurante;

import java.util.List;

@Dao
public interface RestauranteDAO {

    // Insertar un nuevo restaurante
    @Insert
    void insertar(Restaurante restaurante);

    // Obtener todos los restaurantes
    @Query("SELECT * FROM Restaurante")
    List<Restaurante> obtenerTodosRestaurantes();

    // Actualizar un restaurante
    @Update
    void actualizar(Restaurante restaurante);

    // Eliminar un restaurante
    @Delete
    void eliminar(Restaurante restaurante);
}
