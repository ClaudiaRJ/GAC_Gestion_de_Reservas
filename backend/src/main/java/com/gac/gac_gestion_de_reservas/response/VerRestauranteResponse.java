package com.gac.gac_gestion_de_reservas.response;

public record VerRestauranteResponse(Long id,
                                     String cif,
                                     String nombre,
                                     String descripcion,
                                     String direccion,
                                     String provincia,
                                     String ciudad,
                                     String telefono,
                                     String email,
                                     String usuarioResponsable) {
}
