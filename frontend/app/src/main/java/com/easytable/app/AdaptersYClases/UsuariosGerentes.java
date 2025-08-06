package com.easytable.app.AdaptersYClases;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class UsuariosGerentes {
    @PrimaryKey(autoGenerate = true)  // ID autogenerado
    private int id;

    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private String password;
    private String usuario;

    // Constructor
    public UsuariosGerentes(String email, String password, String nombre, String apellido, String telefono, String usuario) {
        this.email = email;
        this.password = password;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Validación simple
    public boolean isValid() {
        return email != null && email.contains("@") && password.length() >= 6;
    }

    // Método toString para una representación más fácil del objeto
    @Override
    public String toString() {
        return "UsuarioGerente{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono='" + telefono + '\'' +
                ", password='" + password + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
