package com.easytable.app.AdaptersYClases.requests.empleados;

public class UpdateRolEmpleadoRequest {
    private Long idEmpleado;
    private String nuevoRol;

    public UpdateRolEmpleadoRequest(Long idEmpleado, String nuevoRol) {
        this.idEmpleado = idEmpleado;
        this.nuevoRol = nuevoRol;
    }

    // Getters y Setters
    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getNuevoRol() { return nuevoRol; }
    public void setNuevoRol(String nuevoRol) { this.nuevoRol = nuevoRol; }
}