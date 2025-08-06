package com.easytable.app.AdaptersYClases.requests.empleados;

public class UpdateGerenteRequest {
    private String nuevoGerenteEmail; // El email del usuario que se convertirá en gerente.

    public UpdateGerenteRequest(String nuevoGerenteEmail) {
        this.nuevoGerenteEmail = nuevoGerenteEmail;
    }

    // Getters y Setters
    public String getNuevoGerenteEmail() { return nuevoGerenteEmail; }
    public void setNuevoGerenteEmail(String nuevoGerenteEmail) { this.nuevoGerenteEmail = nuevoGerenteEmail; }
}