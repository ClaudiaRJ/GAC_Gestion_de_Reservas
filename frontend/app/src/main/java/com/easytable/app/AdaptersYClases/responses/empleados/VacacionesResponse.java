package com.easytable.app.AdaptersYClases.responses.empleados;

import com.google.gson.annotations.SerializedName;

public class VacacionesResponse {
    @SerializedName("comienzoVacaciones")
    private String comienzoVacaciones; // Formato "YYYY-MM-DD" o LocalDate
    @SerializedName("finalVacaciones")
    private String finalVacaciones;   // Formato "YYYY-MM-DD" o LocalDate

    public VacacionesResponse(String comienzoVacaciones, String finalVacaciones) {
        this.comienzoVacaciones = comienzoVacaciones;
        this.finalVacaciones = finalVacaciones;
    }

    public VacacionesResponse() {
    }

    // --- Getters ---
    public String getComienzoVacaciones() { return comienzoVacaciones; }
    public String getFinalVacaciones() { return finalVacaciones; }

    // --- Setters ---
    public void setComienzoVacaciones(String comienzoVacaciones) { this.comienzoVacaciones = comienzoVacaciones; }
    public void setFinalVacaciones(String finalVacaciones) { this.finalVacaciones = finalVacaciones; }

    @Override
    public String toString() {
        return "VacacionesResponse{" +
                "comienzoVacaciones='" + comienzoVacaciones + '\'' +
                ", finalVacaciones='" + finalVacaciones + '\'' +
                '}';
    }
}