package com.easytable.app.AdaptersYClases.responses.menuPrincipal;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("message") // Campo para un mensaje de éxito/información
    private String message;
    @SerializedName("token")   // Campo para el token JWT
    private String token;
    @SerializedName("userId") // Opcional: El ID del usuario recién registrado
    private Long userId;
    @SerializedName("userEmail") // Opcional: El email del usuario
    private String userEmail;


    /**
     * Constructor para RegisterResponse.
     *
     * @param message El mensaje de la respuesta (ej. "Registro exitoso").
     * @param token El token JWT generado para el usuario.
     * @param userId El ID del usuario registrado.
     * @param userEmail El email del usuario registrado.
     */
    public RegisterResponse(String message, String token, Long userId, String userEmail) {
        this.message = message;
        this.token = token;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    // Constructor vacío
    public RegisterResponse() {
    }

    // --- Getters ---
    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    // --- Setters ---
    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "message='" + message + '\'' +
                ", token='[PROTECTED]'" + // No mostrar el token completo en logs por seguridad
                ", userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}