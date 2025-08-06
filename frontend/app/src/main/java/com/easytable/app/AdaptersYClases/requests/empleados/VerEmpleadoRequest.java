package com.easytable.app.AdaptersYClases.requests.empleados;

import com.google.gson.annotations.SerializedName;

public class VerEmpleadoRequest {
    @SerializedName("idEmpleado") // Este nombre debe coincidir con el campo que espera tu backend en el Request
    private Long idEmpleado;

    public VerEmpleadoRequest(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public VerEmpleadoRequest() {
    }

    // --- Getters ---
    public Long getIdEmpleado() {
        return idEmpleado;
    }

    // --- Setters ---
    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @Override
    public String toString() {
        return "VerEmpleadoRequest{" +
                "idEmpleado=" + idEmpleado +
                '}';
    }
}