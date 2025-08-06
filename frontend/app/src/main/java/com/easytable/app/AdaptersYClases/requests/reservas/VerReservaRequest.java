package com.easytable.app.AdaptersYClases.requests.reservas;

import com.google.gson.annotations.SerializedName;

public class VerReservaRequest {
    @SerializedName("idReserva")
    private Long idReserva;

    public VerReservaRequest(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }
}

