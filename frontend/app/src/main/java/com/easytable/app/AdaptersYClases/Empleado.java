package com.easytable.app.AdaptersYClases;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Empleado")
public class Empleado {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    public Empleado(String nombre, String apellido, String email, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
    }

    public Empleado(Long id, String nombre, String apellido, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
    }

    // --- Getters (necesarios para el adaptador) ---
    public Long getId() { return id; } // Si incluyes ID
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }

    // --- Setters (si son necesarios) ---
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}

