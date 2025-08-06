package com.gac.gac_gestion_de_reservas.response;

import com.gac.gac_gestion_de_reservas.reservas.EstadoReserva;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public record VerReservaResponse(VerRestauranteResponse verRestauranteResponse,
                                 VerUsuarioResponse verUsuarioResponse,
                                 LocalDate fecha,
                                 LocalTime hora,
                                 EstadoReserva estadoReserva) implements Serializable {
}
