package com.easytable.app.AdaptersYClases.requests.empleados;

public class EliminarEmpleadoRequest {
    private String email; // Debe coincidir con el nombre de campo que espera tu backend

    public EliminarEmpleadoRequest(String email) {
        this.email = email;
    }

    // Getter
    public String getEmail() {
        return email;
    }

    // Setter (si es necesario)
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EliminarEmpleadoRequest{" +
                "email='" + email + '\'' +
                '}';
    }
}