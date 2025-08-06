// package com.easytable.app.AdaptersYClases.responses;
// O donde tengas tus clases de respuesta
package com.easytable.app.AdaptersYClases.responses.usuarios; // Asumo que va aquí

import com.google.gson.annotations.SerializedName;

public class UsuarioDatos {
    @SerializedName("id") // Este es el ID del usuario
    private Long id;

    @SerializedName("email")
    private String email;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apellido")
    private String apellido;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("rolUsuario")
    private String rolUsuario; // O el tipo de enum que uses en backend, si lo mapeas a String

    // Constructor vacío
    public UsuarioDatos() {}

    // Getters (necesarios para Gson)
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    // Setters (opcionales si solo deserializas)
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    @Override
    public String toString() {
        return "UsuarioDatos{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono='" + telefono + '\'' +
                ", rolUsuario='" + rolUsuario + '\'' +
                '}';
    }
}