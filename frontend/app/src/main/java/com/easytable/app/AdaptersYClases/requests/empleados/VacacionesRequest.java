package com.easytable.app.AdaptersYClases.requests.empleados;

public class VacacionesRequest {
    private String fechaInicio;
    private String fechaFin;
    private String motivo;

    public VacacionesRequest(String fechaInicio, String fechaFin, String motivo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
    }

    // Getters y Setters
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}