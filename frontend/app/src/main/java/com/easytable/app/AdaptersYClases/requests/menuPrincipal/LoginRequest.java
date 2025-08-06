package com.easytable.app.AdaptersYClases.requests.menuPrincipal;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("email") // Debe coincidir con el nombre del campo esperado en el backend
    private String email;
    @SerializedName("password") // Debe coincidir con el nombre del campo esperado en el backend
    private String password;

    /**
     * Constructor para LoginRequest.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Constructor vacío (necesario para algunas operaciones de Gson o Retrofit)
    public LoginRequest() {
    }

    // --- Getters ---
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // --- Setters ---
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" + // No mostrar la contraseña en logs
                '}';
    }
}