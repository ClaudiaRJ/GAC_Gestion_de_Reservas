package com.gac.gac_gestion_de_reservas.requests;

import java.time.LocalDate;

public record VacacionesRequest(LocalDate comienzoVacaciones,
                                LocalDate finalVacaciones) {
}
