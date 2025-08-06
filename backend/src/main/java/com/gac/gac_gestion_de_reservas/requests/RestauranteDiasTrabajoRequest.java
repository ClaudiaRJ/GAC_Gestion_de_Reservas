package com.gac.gac_gestion_de_reservas.requests;

import com.gac.gac_gestion_de_reservas.restaurantes.horarios.HorarioTrabajo;

import java.time.DayOfWeek;
import java.util.List;

public record RestauranteDiasTrabajoRequest(DayOfWeek dia,
                                            List<HorarioTrabajo> horarioTrabajos
                                             ) {
}
