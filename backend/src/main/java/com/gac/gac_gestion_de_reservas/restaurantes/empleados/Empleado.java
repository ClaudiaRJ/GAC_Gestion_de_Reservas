package com.gac.gac_gestion_de_reservas.restaurantes.empleados;

import com.gac.gac_gestion_de_reservas.restaurantes.Restaurante;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Empleado {

    @Id
    @SequenceGenerator(
            name = "empleado_sequence",
            sequenceName = "empleado_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "empleado_sequence"
    )
    private long id;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
    private LocalDate createdAt;
    private String createdBy;
    private LocalDate updatedAt;
    private String updatedBy;
    private LocalDate deletedAt;
    private String deletedBy;
    private boolean enabled;

    public Empleado(Usuario usuario, Restaurante restaurante, LocalDate createdAt, String createdBy) {
        this.usuario = usuario;
        this.restaurante = restaurante;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.enabled = true;
    }

}
