package com.gac.gac_gestion_de_reservas.auth;

import com.gac.gac_gestion_de_reservas.requests.EmailRequest;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioService;
import com.gac.gac_gestion_de_reservas.requests.AuthenticationRequest;
import com.gac.gac_gestion_de_reservas.requests.ResetPasswordRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gac/auth/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UsuarioService usuarioService;

    public AuthenticationController(AuthenticationService authenticationService, UsuarioService usuarioService) {
        this.authenticationService = authenticationService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest request) {
        return ResponseEntity.ok( usuarioService.forgotPassword(request.email()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        usuarioService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok("Password actualizado!");
    }

}
