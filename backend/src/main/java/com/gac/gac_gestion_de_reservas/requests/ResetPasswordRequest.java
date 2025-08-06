package com.gac.gac_gestion_de_reservas.requests;

public record ResetPasswordRequest(String token, String newPassword) {
}
