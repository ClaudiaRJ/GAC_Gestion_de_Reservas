package com.gac.gac_gestion_de_reservas.usuarios;
import com.gac.gac_gestion_de_reservas.requests.ConfirmationTokenRequest;
import com.gac.gac_gestion_de_reservas.requests.UpdateEmailRequest;
import com.gac.gac_gestion_de_reservas.requests.UpdatePasswordRequest;
import com.gac.gac_gestion_de_reservas.requests.UpdateUsuarioRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "api/gac/usuario")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest request) {
        usuarioService.updatePassword(request.id(), request.newPassword());
        return ResponseEntity.ok("Password actualizado!");
    }

    @PostMapping("/update-email")
    public ResponseEntity<String> updateEmail(@RequestBody UpdateEmailRequest request) {
        usuarioService.updateEmail(request.id(), request.email());
        return ResponseEntity.ok("Enviado email para confirmar actualización!");
    }

    @PostMapping(path = "confirm-update-email")
    public ResponseEntity<String> confirm(@RequestBody ConfirmationTokenRequest request) {
        usuarioService.confirmUpdateEmail(request.token());
        return ResponseEntity.ok("Email actualizado");
    }

    @PostMapping("update-usuario")
    public ResponseEntity<?> updateUsuario(@RequestBody UpdateUsuarioRequest request) {
        return ResponseEntity.ok()
                .body( usuarioService.updateUsuario(request.id(), request.nombre(), request.apellido(), request.telefono()));
    }

}
