package com.gac.gac_gestion_de_reservas.usuarios.token;

import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class ResetPasswordToken {

    @Id
    @SequenceGenerator(
            name = "reset_password_token_sequence",
            sequenceName = "reset_password_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reset_password_token_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "usuario_id"
    )

    private Usuario usuario;

    public ResetPasswordToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, Usuario usuario) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.usuario = usuario;
    }
}
