package com.gac.gac_gestion_de_reservas.restaurantes.horarios;

import com.gac.gac_gestion_de_reservas.restaurantes.Restaurante;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vacaciones {

    @Id
    @SequenceGenerator(
            name = "vacaciones_sequence",
            sequenceName = "vacaciones_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "vacaciones_sequence"
    )
    private long id;
    private LocalDate comienzoVacaciones;
    private LocalDate finalVacaciones;
    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
    private String createdBy;
    private LocalDate creationDate;
    private String modifiedBy;
    private LocalDate modificationDate;

    public Vacaciones(LocalDate comienzoVacaciones, LocalDate finalVacaciones, Restaurante restaurante, String createdBy) {
        this.comienzoVacaciones = comienzoVacaciones;
        this.finalVacaciones = finalVacaciones;
        this.restaurante = restaurante;
        this.createdBy = createdBy;
        this.creationDate = LocalDate.now();
    }
}
