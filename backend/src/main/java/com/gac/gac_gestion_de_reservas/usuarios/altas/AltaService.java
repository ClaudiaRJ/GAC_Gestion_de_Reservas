package com.gac.gac_gestion_de_reservas.usuarios.altas;


import com.gac.gac_gestion_de_reservas.usuarios.token.ConfirmationToken;
import com.gac.gac_gestion_de_reservas.usuarios.token.ConfirmationTokenRepository;
import com.gac.gac_gestion_de_reservas.email.EmailService;
import com.gac.gac_gestion_de_reservas.usuarios.RolUsuario;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioRepository;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AltaService {

    private final UsuarioService usuarioService;
    private final EmailValidator emailValidator;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;


    public String registrar(SolicitudAltaUsuario solicitud) {

        boolean isValidEmail = emailValidator.test(solicitud.email());
        if (!isValidEmail) {
            throw new IllegalStateException("El email no es valido");
        }

        String token = usuarioService.altaUsuario(
                new Usuario(
                        solicitud.nombre(),
                        solicitud.apellido(),
                        solicitud.telefono(),
                        solicitud.email(),
                        solicitud.password(),
                        RolUsuario.CLIENTE
                )
        );

        String link = "https://easytable.ddns.net:35800/api/gac/altas/confirm?token=" + token;
        emailService.sendConfirmationToken(solicitud.email(), emailService.emailConfirmationToken(solicitud.nombre(), link));
        return token;
    }

    public String reenviarConfirmationToken(String email) {

        Optional<Usuario> usuarioExiste = usuarioRepository.findByEmail(email);
        if (usuarioExiste.isEmpty()) {
            throw new IllegalStateException("Email no registrado");
        }
        Usuario usuario = usuarioExiste.get();
        if (usuario.isEnabled()){
            throw new IllegalStateException("Cuenta ya activa");
        }

        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenRepository.findByUsuario(usuarioExiste.get());
        if (confirmationTokenOptional.isPresent()) {
            ConfirmationToken confirmationToken = confirmationTokenOptional.get();
            if (confirmationToken.getConfirmedAt() == null) {
                confirmationToken.setConfirmedAt(LocalDateTime.now());
                confirmationTokenRepository.save(confirmationToken);
            }
        }

        String token = usuarioService.newConfirmationToken(usuario);
        String link = "https://easytable.ddns.net:35800/api/gac/altas/confirm?token=" + token;
        emailService.sendConfirmationToken(usuario.getEmail(), emailService.emailConfirmationToken(usuario.getNombre(),link));
        return token;
    }
}