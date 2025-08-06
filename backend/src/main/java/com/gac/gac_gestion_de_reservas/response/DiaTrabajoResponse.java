package com.gac.gac_gestion_de_reservas.response;

import com.gac.gac_gestion_de_reservas.restaurantes.horarios.HorarioTrabajo;

import java.time.DayOfWeek;
import java.util.List;

public record DiaTrabajoResponse(DayOfWeek dia,
                                 List<HorarioTrabajo> horarios) {
}
