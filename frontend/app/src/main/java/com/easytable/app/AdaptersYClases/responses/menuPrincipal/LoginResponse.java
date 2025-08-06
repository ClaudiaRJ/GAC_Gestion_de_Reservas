// package com.easytable.app.AdaptersYClases.responses.LoginResponse.java
package com.easytable.app.AdaptersYClases.responses.menuPrincipal;

import com.easytable.app.AdaptersYClases.responses.usuarios.UsuarioDatos;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String token;
    private String role; // Mapea a 'role' directamente si tu backend lo envía así
    private String name; // Mapea a 'name' directamente si tu backend lo envía así

    @SerializedName("usuarioDatos") // <-- ¡Campo clave ahora!
    private UsuarioDatos usuarioDatos;

    public LoginResponse() {}

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UsuarioDatos getUsuarioDatos() {
        return usuarioDatos;
    }

    public void setUsuarioDatos(UsuarioDatos usuarioDatos) {
        this.usuarioDatos = usuarioDatos;
    }

    // El userId ahora se obtiene del objeto UsuarioDatos
    public Long getUserId() {
        return (usuarioDatos != null) ? usuarioDatos.getId() : null;
    }


    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + (token != null ? "[REDACTED]" : "null") + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", usuarioDatos=" + (usuarioDatos != null ? usuarioDatos.toString() : "null") +
                '}';
    }
}