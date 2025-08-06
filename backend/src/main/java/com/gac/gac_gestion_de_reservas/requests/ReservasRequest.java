package com.gac.gac_gestion_de_reservas.requests;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservasRequest(long restauranteId,
                              long clienteId,
                              LocalDate fecha,
                              LocalTime hora,
                              int personasReserva) {
}
