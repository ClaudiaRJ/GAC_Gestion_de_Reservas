package com.gac.gac_gestion_de_reservas.usuarios;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UsuarioDTOMapper implements Function<Usuario, UsuarioDatos> {
    @Override
    public UsuarioDatos apply(Usuario usuario) {
        return new UsuarioDatos(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getTelefono(),
                usuario.getEmail(),
                usuario.getRolUsuario().name()
        );
    }
}
