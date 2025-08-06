package com.gac.gac_gestion_de_reservas.usuarios.altas;

import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioService;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioUtil;
import com.gac.gac_gestion_de_reservas.requests.EmailRequest;
import com.gac.gac_gestion_de_reservas.requests.ConfirmationTokenRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/gac/altas")
@AllArgsConstructor
public class AltasController {

    private final AltaService altaService;
    private final UsuarioUtil usuarioUtil;
    private final UsuarioService usuarioService;

    @PostMapping("alta-usuario")
    public String altaUsuario(@RequestBody SolicitudAltaUsuario usuario) {
        return altaService.registrar(usuario);
    }

    @PostMapping(path = "confirm")
    public ResponseEntity<String> confirm(@RequestBody ConfirmationTokenRequest request) {
        Usuario usuario = usuarioUtil.verificarToken(request.token());
        usuarioService.enableUsuario(usuario.getEmail());
        return ResponseEntity.ok("Cuenta activada!");
    }

    @PostMapping(path = "reenviarToken")
    public ResponseEntity<String> reenviarToken(@RequestBody EmailRequest request) {
        return ResponseEntity.ok(altaService.reenviarConfirmationToken(request.email()));
    }

}
