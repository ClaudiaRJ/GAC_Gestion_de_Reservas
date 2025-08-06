package com.easytable.app.AdaptersYClases.requests.menuPrincipal;

import com.google.gson.annotations.SerializedName;

public class RegisterClientRequest {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("apellido")
    private String apellido;
    @SerializedName("telefono")
    private String telefono;

    /**
     * Constructor para RegisterClientRequest.
     *
     * @param email El correo electrónico del nuevo cliente.
     * @param password La contraseña del nuevo cliente.
     * @param nombre El nombre del nuevo cliente.
     * @param apellido El apellido del nuevo cliente.
     * @param telefono El número de teléfono del nuevo cliente.
     */
    public RegisterClientRequest(String email, String password, String nombre, String apellido, String telefono) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    // Constructor vacío
    public RegisterClientRequest() {
    }

    // --- Getters ---
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    // --- Setters ---
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return "RegisterClientRequest{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}