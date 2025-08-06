package com.gac.gac_gestion_de_reservas.usuarios;

import com.gac.gac_gestion_de_reservas.response.VerUsuarioResponse;
import com.gac.gac_gestion_de_reservas.usuarios.altas.EmailValidator;
import com.gac.gac_gestion_de_reservas.usuarios.altas.SolicitudAltaUsuario;
import com.gac.gac_gestion_de_reservas.usuarios.token.ConfirmationToken;
import com.gac.gac_gestion_de_reservas.usuarios.token.ConfirmationTokenService;
import com.gac.gac_gestion_de_reservas.email.EmailSender;
import com.gac.gac_gestion_de_reservas.email.EmailService;
import com.gac.gac_gestion_de_reservas.usuarios.token.ResetPasswordToken;
import com.gac.gac_gestion_de_reservas.usuarios.token.ResetPasswordTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "No encontrado usuario con el email %s ";
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final EmailSender emailSender;
    private final EmailService emailService;
    private final EmailValidator emailValidator;
    private final NuevoEmailRepository nuevoEmailRepository;
    private final UsuarioUtil usuarioUtil;
    private final UsuarioDTOMapper usuarioDTOMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findUsuarioByEmail(email);
    }

    /**
     * Funcion para dar de alta a un usuario
     * usuarioExiste es un objeto para verificar si el email ya esta dado de alta y tambien si la cuenta esta activa.
     * @param usuario a ser dado de alta
     * @return String con token de confirmacion
     */
    public String altaUsuario(Usuario usuario) {

        Optional<Usuario> usuarioExiste = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioExiste.isPresent()) {
            if (usuarioExiste.get().isEnabled()){
               throw new IllegalStateException("Email ya registrado y cuenta activa!");
            }
            throw new IllegalStateException("Email ya registrado, pero la cuenta no esta activa!!");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(usuario.getPassword());

        usuario.setPassword(encodedPassword);

        usuarioRepository.save(usuario);

        return newConfirmationToken(usuario);
    }

    public Usuario altaUsuarioGerente(SolicitudAltaUsuario solicitud){

        boolean isValidEmail = emailValidator.test(solicitud.email());
        if (!isValidEmail) {
            throw new IllegalStateException("El email no es valido");
        }

        String token = altaUsuario(
                new Usuario(
                        solicitud.nombre(),
                        solicitud.apellido(),
                        solicitud.telefono(),
                        solicitud.email(),
                        solicitud.password(),
                        RolUsuario.GERENTE
                ));

        String link = "https://easytable.ddns.net:35800/api/gac/altas/confirm?token=" + token;
        emailService.sendConfirmationToken(solicitud.email(), emailService.emailConfirmationToken(solicitud.nombre(), link));
        return usuarioRepository.findUsuarioByEmail(solicitud.email());
    }

    public String newConfirmationToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(8),
                usuario
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public void enableUsuario(String email) {
         usuarioRepository.enableUsuario(email);
    }

    public Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return findUsuarioByEmail(userDetails.getUsername());
        }
        throw new RuntimeException("Usuario no autenticado");
    }

    public void updateEmail(Long id, String email){

        Optional<Usuario> usuarioExiste = usuarioRepository.findByEmail(email);

        if (usuarioExiste.isPresent()) {
            throw new IllegalStateException("Email ya registrado!");
        }

        usuarioExiste = usuarioRepository.findById(id);

        if (usuarioExiste.isEmpty()) {
            throw new IllegalStateException("Usuario no encontrado!");
        }
            Usuario usuario = usuarioExiste.get();

        String token = newConfirmationToken(usuario);

        NuevoEmail nuevoEmail = new NuevoEmail(
                usuario,
                email,
                token
                );

        nuevoEmailRepository.save(nuevoEmail);

        String link = "https://easytable.ddns.net:35800/api/gac/altas/update-email?token=" + token;

        emailService.sendUpdateEmailToken(nuevoEmail.getNuevoEmail(), emailService.emailUpdateEmailToken(usuario.getNombre(), link));

    }

    public void confirmUpdateEmail(String token) {

        Usuario usuario = usuarioUtil.verificarToken(token);

        Optional<NuevoEmail> nuevoEmail = nuevoEmailRepository.findByToken(token);

        if (nuevoEmail.isEmpty()) {
            throw new IllegalStateException("Token no encontrado");
        }

        usuario.setEmail(nuevoEmail.get().getNuevoEmail());

        usuarioRepository.save(usuario);

    }

    public String forgotPassword(String email) {

        Optional<Usuario> usuarioExiste = usuarioRepository.findByEmail(email);

        if (usuarioExiste.isEmpty()) {
            throw new IllegalStateException("Email no encontrado!");
        }

        if (!usuarioExiste.get().isEnabled()) {
            throw new IllegalStateException("Email registrado, pero la cuenta no esta activa!!");
        }

        Usuario usuario = usuarioExiste.get();
        String token = newResetPasswordToken(usuario);

        String link = "https://easytable.ddns.net:35800/api/gac/usuario/setpassword?token=" + token;
        emailSender.sendResetPasswordToken(usuario.getEmail(), emailService.emailResetPassword(usuario.getNombre(),link));

        return token;
    }

    public void resetPassword(String token, String newPassword) {
        ResetPasswordToken resetPasswordToken = resetPasswordTokenService
                .getResetPasswordToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token invalido!"));

        if (resetPasswordToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token ya utilizado!");
        }

        LocalDateTime expiredAt = resetPasswordToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token caducado");
        }

        resetPasswordTokenService.setConfirmedAt(token);

        Usuario usuario = resetPasswordToken.getUsuario();

        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);

        usuario.setPassword(encodedPassword);

        usuarioRepository.save(usuario);

    }

    protected void updatePassword(Long id, String newPassword){

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalStateException(String.format(USER_NOT_FOUND_MSG, id)));

        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);

        usuario.setPassword(encodedPassword);

        usuarioRepository.save(usuario);

    }

    public String newResetPasswordToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(8),
                usuario
        );

        resetPasswordTokenService.saveResetPasswordToken(resetPasswordToken);

        return resetPasswordToken.getToken();
    }

    public UsuarioDatos updateUsuario(Long id, String nombre, String apellido, String telefono) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);
        if (usuarioExiste.isEmpty()) {
            throw new IllegalStateException("Usuario no encontrado!");
        }

        Usuario usuario = usuarioExiste.get();

        if (!usuario.getNombre().equals(nombre) && nombre!=null) {
            usuario.setNombre(nombre);
        }
        if (!usuario.getApellido().equals(apellido) && apellido!=null) {
            usuario.setApellido(apellido);
        }
        if (!usuario.getTelefono().equals(telefono) && telefono!=null) {
            usuario.setTelefono(telefono);
        }

        usuarioRepository.save(usuario);
        return usuarioDTOMapper.apply(usuario);
    }

    public VerUsuarioResponse verUsuarioByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(IllegalStateException::new);
        return new VerUsuarioResponse(usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono());
    }
}
