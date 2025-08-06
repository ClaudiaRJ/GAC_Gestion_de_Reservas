package com.gac.gac_gestion_de_reservas.requests;

import com.gac.gac_gestion_de_reservas.usuarios.RolUsuario;

public record UpdateRolEmpleadoRequest(String email, RolUsuario rolUsuario) {
}
