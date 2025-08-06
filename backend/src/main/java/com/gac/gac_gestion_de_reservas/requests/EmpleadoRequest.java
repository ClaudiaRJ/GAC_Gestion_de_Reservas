package com.gac.gac_gestion_de_reservas.requests;

import com.gac.gac_gestion_de_reservas.usuarios.RolUsuario;

public record EmpleadoRequest(
        String nombre,
        String apellido,
        String telefono,
        String email,
        RolUsuario rolUsuario
) {

}
