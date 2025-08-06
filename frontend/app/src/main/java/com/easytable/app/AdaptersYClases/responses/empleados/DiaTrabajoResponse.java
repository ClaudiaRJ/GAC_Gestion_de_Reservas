package com.easytable.app.AdaptersYClases.responses.empleados;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DiaTrabajoResponse {
    @SerializedName("dia")
    private String dia; // Por ejemplo: "LUNES", "MARTES", etc.
    @SerializedName("horario")
    private List<String> horario; // Una lista de strings que representan los rangos de horario (ej. ["10:00-14:00", "17:00-22:00"])

    public DiaTrabajoResponse(String dia, List<String> horario) {
        this.dia = dia;
        this.horario = horario;
    }

    public DiaTrabajoResponse() {
    }

    // --- Getters ---
    public String getDia() { return dia; }
    public List<String> getHorario() { return horario; }

    // --- Setters ---
    public void setDia(String dia) { this.dia = dia; }
    public void setHorario(List<String> horario) { this.horario = horario; }

    @Override
    public String toString() {
        return "DiaTrabajoResponse{" +
                "dia='" + dia + '\'' +
                ", horario=" + horario +
                '}';
    }
}