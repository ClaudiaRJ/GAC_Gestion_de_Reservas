// com.easytable.app.AdaptersYClases.Reserva.java
package com.easytable.app.AdaptersYClases;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.easytable.app.API.RestauranteApi; // Importa la clase RestauranteApi
import com.google.gson.annotations.SerializedName; // Necesario para mapear nombres diferentes en JSON

public class Reserva implements Serializable {

    private Long id;
    @SerializedName("restaurante") // Este es el nombre del objeto anidado 'restaurante' en el JSON
    private RestauranteApi objetoRestaurante; // Usamos este nombre para representar el objeto Restaurante de la API

    private String fecha;
    private String hora;
    @SerializedName("cantidadPersonas") // Mapea "cantidadPersonas" del JSON a "comensales" en Java
    private int comensales;
    private String emailUsuario; // Cambiado de 'usuarioEmail' a 'emailUsuario'
    @SerializedName("estadoReserva") // Mapea "estadoReserva" del JSON a "estado" en Java
    private String estado; // Nombre del campo para el String del estado de la reserva

    // Constructor vacío (necesario para Gson)
    public Reserva() {
    }

    // Constructor completo
    public Reserva(Long id, RestauranteApi objetoRestaurante, String fecha, String hora, int comensales, String emailUsuario, String estado) {
        this.id = id;
        this.objetoRestaurante = objetoRestaurante;
        this.fecha = fecha;
        this.hora = hora;
        this.comensales = comensales;
        this.emailUsuario = emailUsuario;
        this.estado = estado;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public RestauranteApi getObjetoRestaurante() { return objetoRestaurante; } // Getter para el objeto RestauranteApi
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public int getComensales() { return comensales; } // Obtiene la cantidad de comensales (mapeado de 'cantidadPersonas' del JSON)
    public String getEmailUsuario() { return emailUsuario; } // Getter para el email del usuario
    public String getEstado() { return estado; } // Getter para el estado de la reserva

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setObjetoRestaurante(RestauranteApi objetoRestaurante) { this.objetoRestaurante = objetoRestaurante; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setHora(String hora) { this.hora = hora; }
    public void setComensales(int comensales) { this.comensales = comensales; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean estaActiva() {
        boolean esEstadoActivo = "PENDIENTE".equalsIgnoreCase(estado) || "CONFIRMADA".equalsIgnoreCase(estado);

        if (!esEstadoActivo) {
            return false;
        }

        try {
            LocalDate fechaReserva = LocalDate.parse(fecha);
            LocalTime horaReserva = LocalTime.parse(hora);
            LocalDate fechaActual = LocalDate.now();
            LocalTime horaActual = LocalTime.now();

            if (fechaReserva.isAfter(fechaActual)) {
                return true;
            } else if (fechaReserva.isEqual(fechaActual)) {
                return horaReserva.isAfter(horaActual);
            } else {
                return false;
            }
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear fecha u hora de reserva: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", restaurante=" + (objetoRestaurante != null ? objetoRestaurante.getNombre() : "N/A") +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", comensales=" + comensales +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}