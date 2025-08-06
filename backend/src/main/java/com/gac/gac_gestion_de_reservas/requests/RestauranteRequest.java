package com.gac.gac_gestion_de_reservas.requests;



public record RestauranteRequest(
        String nombre,
        String CIFRestaurante,
        String descripcionRestaurante,
        String telefonoRestaurante,
        String direccionRestaurante,
        String provinciaRestaurante,
        String ciudadRestaurante,
        String emailRestaurante,

        //datos del gerente
        String nombreGerente,
        String apellidoGerente,
        String telefonoGerente,
        String emailGerente,
        String passwordGerente
) {
}
