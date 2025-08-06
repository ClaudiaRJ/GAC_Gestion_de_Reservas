package com.easytable.app.AdaptersYClases.requests.empleados;

public class DesactivarEmpleadoRequest {
    private String emailEmpleado;

    public DesactivarEmpleadoRequest(String emailEmpleado) {
        this.emailEmpleado = emailEmpleado;
    }

    // Getters y Setters
    public String getEmailEmpleado() { return emailEmpleado; }
    public void setEmailEmpleado(String emailEmpleado) { this.emailEmpleado = emailEmpleado; }
}