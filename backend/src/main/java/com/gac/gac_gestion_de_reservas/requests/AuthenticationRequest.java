package com.gac.gac_gestion_de_reservas.requests;

public record AuthenticationRequest(
        String email,
        String password
) {
}
