package com.gac.gac_gestion_de_reservas.auth;

import com.gac.gac_gestion_de_reservas.jwt.JWTUtil;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioDTOMapper;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioDatos;
import com.gac.gac_gestion_de_reservas.requests.AuthenticationRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioDTOMapper usuarioDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 UsuarioDTOMapper usuarioDTOMapper,
                                 JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioDTOMapper = usuarioDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
            Usuario principal = (Usuario) authentication.getPrincipal();
            UsuarioDatos usuarioDatos = usuarioDTOMapper.apply(principal);
            String token = jwtUtil.issueToken(usuarioDatos.email(), usuarioDatos.rolUsuario());
            return new AuthenticationResponse(token, usuarioDatos);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Credenciales invalidas!");
        }
    }

}
