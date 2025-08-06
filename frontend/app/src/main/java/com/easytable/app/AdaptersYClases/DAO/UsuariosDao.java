package com.easytable.app.AdaptersYClases.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.easytable.app.AdaptersYClases.UsuariosGerentes;

import java.util.List;

@Dao
public interface UsuariosDao {
    // Insertar un nuevo usuario
    @Insert
    void insertarUsuario(UsuariosGerentes usuario);

    // Consultar todos los usuarios
    @Query("SELECT * FROM usuarios")
    List<UsuariosGerentes> obtenerTodosLosUsuarios();

    // Obtener un usuario por su correo electrónico
    @Query("SELECT * FROM usuarios WHERE email = :email")
    UsuariosGerentes obtenerUsuarioPorEmail(String email);

    // Actualizar un usuario
    @Update
    void actualizarUsuario(UsuariosGerentes usuario);

    // Eliminar un usuario
    @Delete
    void eliminarUsuario(UsuariosGerentes usuario);
}
