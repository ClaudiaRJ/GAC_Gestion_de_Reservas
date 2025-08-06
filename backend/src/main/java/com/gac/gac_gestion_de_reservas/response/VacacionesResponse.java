package com.gac.gac_gestion_de_reservas.response;

import java.time.LocalDate;

public record VacacionesResponse(LocalDate comienzoVacaciones,
                                 LocalDate finalVacaciones) {
}
