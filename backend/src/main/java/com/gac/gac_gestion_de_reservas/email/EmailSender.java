package com.gac.gac_gestion_de_reservas.email;

public interface EmailSender {
    void sendConfirmationToken(String to, String email);
    void sendResetPasswordToken(String to, String email);
    void sendUpdateEmailToken(String to, String email);
}
