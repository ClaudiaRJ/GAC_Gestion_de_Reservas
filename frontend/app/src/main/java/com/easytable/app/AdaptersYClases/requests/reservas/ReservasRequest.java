// com.easytable.app.AdaptersYClases.requests.ReservasRequest.java (Android)
package com.easytable.app.AdaptersYClases.requests.reservas;

import com.google.gson.annotations.SerializedName;

public class ReservasRequest {
    @SerializedName("restauranteId") // <-- ¡CAMBIAR A ID!
    private Long restauranteId; // Tipo Long para el ID

    @SerializedName("clienteId") // <-- ¡CAMBIAR A ID!
    private Long clienteId; // Tipo Long para el ID

    @SerializedName("fecha")
    private String fecha; // Formato "YYYY-MM-DD"
    @SerializedName("hora")
    private String hora;  // Formato "HH:MM"
    @SerializedName("cantidadPersonas")
    private int cantidadPersonas;

    // Constructor con los nuevos campos
    public ReservasRequest(Long restauranteId, Long clienteId, String fecha, String hora, int cantidadPersonas) {
        this.restauranteId = restauranteId;
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.hora = hora;
        this.cantidadPersonas = cantidadPersonas;
    }

    public ReservasRequest() {
        // Constructor vacío necesario para Gson
    }

    // --- Getters ---
    public Long getRestauranteId() { return restauranteId; } // Getter para el ID
    public Long getClienteId() { return clienteId; } // Getter para el ID
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public int getCantidadPersonas() { return cantidadPersonas; }

    // --- Setters ---
    public void setRestauranteId(Long restauranteId) { this.restauranteId = restauranteId; } // Setter para el ID
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; } // Setter para el ID
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setHora(String hora) { this.hora = hora; }
    public void setCantidadPersonas(int cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }

    @Override
    public String toString() {
        return "ReservasRequest{" +
                "restauranteId=" + restauranteId + // Cambiado
                ", clienteId=" + clienteId +       // Cambiado
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", cantidadPersonas=" + cantidadPersonas +
                '}';
    }
}