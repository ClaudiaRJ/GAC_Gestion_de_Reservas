package com.gac.gac_gestion_de_reservas.usuarios;


public record UsuarioDatos(
         long id,
         String nombre,
         String apellido,
         String telefono,
         String email,
         String rolUsuario
) {
}
