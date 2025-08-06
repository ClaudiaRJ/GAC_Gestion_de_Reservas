package com.gac.gac_gestion_de_reservas.usuarios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class NuevoEmail{

    @Id
    @SequenceGenerator(
            name = "email_sequence",
            sequenceName = "email_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "email_sequence"
    )
    private long id;

    @ManyToOne
    private Usuario usuario;

    private String nuevoEmail;

    private String token;

    public NuevoEmail(Usuario usuario, String nuevoEmail, String token) {
        this.usuario = usuario;
        this.nuevoEmail = nuevoEmail;
        this.token = token;
    }
}
