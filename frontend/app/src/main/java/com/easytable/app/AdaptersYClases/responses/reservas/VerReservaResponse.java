package com.easytable.app.AdaptersYClases.responses.reservas;

public class VerReservaResponse {
    private Long id;
    private String fecha;
    private String hora;
    private int cantidadPersonas;
    private String usuarioEmail; // Para identificar al cliente
    private String estadoReserva; // El estado de la reserva (ej. CONFIRMADA, PENDIENTE, ANULADA)
    private RestauranteResponse restaurante; // Objeto anidado para el restaurante

    // Constructor vacío (necesario para Gson)
    public VerReservaResponse() {
    }

    // Constructor con todos los campos (útil para mapeo)
    public VerReservaResponse(Long id, String fecha, String hora, int cantidadPersonas, String usuarioEmail, String estadoReserva, RestauranteResponse restaurante) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.cantidadPersonas = cantidadPersonas;
        this.usuarioEmail = usuarioEmail;
        this.estadoReserva = estadoReserva;
        this.restaurante = restaurante;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public RestauranteResponse getRestaurante() {
        return restaurante;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setCantidadPersonas(int cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public void setRestaurante(RestauranteResponse restaurante) {
        this.restaurante = restaurante;
    }

    // Clase interna para el DTO del Restaurante anidado
    public static class RestauranteResponse {
        private String nombre;

        public RestauranteResponse() {
        }

        public RestauranteResponse(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }
}