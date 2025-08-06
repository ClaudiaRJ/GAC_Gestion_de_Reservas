package com.gac.gac_gestion_de_reservas.usuarios.altas;

import com.gac.gac_gestion_de_reservas.usuarios.RolUsuario;

public record SolicitudAltaUsuario(
        String nombre,
        String apellido,
        String telefono,
        String email,
        String password,
        RolUsuario rolUsuario) {
}
