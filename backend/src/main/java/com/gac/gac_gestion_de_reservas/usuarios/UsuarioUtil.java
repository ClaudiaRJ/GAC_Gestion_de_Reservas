package com.gac.gac_gestion_de_reservas.usuarios;

import com.gac.gac_gestion_de_reservas.usuarios.token.ConfirmationToken;
import com.gac.gac_gestion_de_reservas.usuarios.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UsuarioUtil {

    private final ConfirmationTokenService confirmationTokenService;

    public Usuario verificarToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);

        return confirmationToken.getUsuario();
    }

}
