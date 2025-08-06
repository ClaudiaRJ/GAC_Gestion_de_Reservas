package com.gac.gac_gestion_de_reservas.response;

import com.gac.gac_gestion_de_reservas.usuarios.RolUsuario;

public record VerEmpleadoResponse(String nombre,
                                  String apellido,
                                  String telefono,
                                  String email,
                                  RolUsuario rolUsuario,
                                  String estado) {
}
