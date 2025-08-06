package com.easytable.app.AdaptersYClases.requests.restaurantes;

public class RestauranteDiasTrabajoRequest {
    private String diaSemana;
    private String horaApertura;
    private String horaCierre;
    private boolean estaAbierto;

    public RestauranteDiasTrabajoRequest(String diaSemana, String horaApertura, String horaCierre, boolean estaAbierto) {
        this.diaSemana = diaSemana;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.estaAbierto = estaAbierto;
    }

    // Getters y Setters
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public String getHoraApertura() { return horaApertura; }
    public void setHoraApertura(String horaApertura) { this.horaApertura = horaApertura; }
    public String getHoraCierre() { return horaCierre; }
    public void setHoraCierre(String horaCierre) { this.horaCierre = horaCierre; }
    public boolean isEstaAbierto() { return estaAbierto; } // Para boolean, getter es isFieldName
    public void setEstaAbierto(boolean estaAbierto) { this.estaAbierto = estaAbierto; }
}