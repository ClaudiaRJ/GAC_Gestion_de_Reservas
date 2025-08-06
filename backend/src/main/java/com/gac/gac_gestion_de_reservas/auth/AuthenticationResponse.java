package com.gac.gac_gestion_de_reservas.auth;


import com.gac.gac_gestion_de_reservas.usuarios.UsuarioDatos;

public record AuthenticationResponse (
        String token,
        UsuarioDatos usuarioDatos){
}
