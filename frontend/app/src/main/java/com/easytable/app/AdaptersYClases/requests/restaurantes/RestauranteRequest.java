package com.easytable.app.AdaptersYClases.requests.restaurantes;

public class RestauranteRequest {
    private String nombreRestaurante;
    private String cifRestaurante;
    private String descripcionRestaurante;

    public RestauranteRequest(String nombreRestaurante, String cifRestaurante, String descripcionRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
        this.cifRestaurante = cifRestaurante;
        this.descripcionRestaurante = descripcionRestaurante;
    }

    // Getters y Setters
    public String getNombreRestaurante() { return nombreRestaurante; }
    public void setNombreRestaurante(String nombreRestaurante) { this.nombreRestaurante = nombreRestaurante; }
    public String getCifRestaurante() { return cifRestaurante; }
    public void setCifRestaurante(String cifRestaurante) { this.cifRestaurante = cifRestaurante; }
    public String getDescripcionRestaurante() { return descripcionRestaurante; }
    public void setDescripcionRestaurante(String descripcionRestaurante) { this.descripcionRestaurante = descripcionRestaurante; }
}