package com.gac.gac_gestion_de_reservas.requests;

public record UpdateRestauranteRequest(String cif,
                                       String nombre,
                                       String descripcion,
                                       String direccion,
                                       String provincia,
                                       String ciudad,
                                       String telefono,
                                       String email) {
}
