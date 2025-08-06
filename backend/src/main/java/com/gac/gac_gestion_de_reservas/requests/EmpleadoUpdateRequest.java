package com.gac.gac_gestion_de_reservas.requests;

public record EmpleadoUpdateRequest(Long idEmpleado,
                                    String nombre,
                                    String apellido,
                                    String telefono) {
}
