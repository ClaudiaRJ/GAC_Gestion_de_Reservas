package com.easytable.app.AdaptersYClases.requests.restaurantes;

public class UpdateRestauranteRequest {
    private Long id; // El ID del restaurante a actualizar. Puede ser opcional si el ID se pasa por PathVariable en el backend.
    private String nombreRestaurante;
    private String descripcionRestaurante;

    public UpdateRestauranteRequest(Long id, String nombreRestaurante, String descripcionRestaurante) {
        this.id = id;
        this.nombreRestaurante = nombreRestaurante;
        this.descripcionRestaurante = descripcionRestaurante;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreRestaurante() { return nombreRestaurante; }
    public void setNombreRestaurante(String nombreRestaurante) { this.nombreRestaurante = nombreRestaurante; }
    public String getDescripcionRestaurante() { return descripcionRestaurante; }
    public void setDescripcionRestaurante(String descripcionRestaurante) { this.descripcionRestaurante = descripcionRestaurante; }
}