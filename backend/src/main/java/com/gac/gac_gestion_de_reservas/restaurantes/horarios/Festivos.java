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
public class Festivos {

    @Id
    @SequenceGenerator(
            name = "festivos_sequence",
            sequenceName = "festivos_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "festivos_sequence"
    )
    private long id;
    private String nombre;
    private LocalDate fechaFestivo;
    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    private String createdBy;
    private LocalDate creationDate;
    private String modifiedBy;
    private LocalDate modificationDate;

    public Festivos(String nombre, LocalDate fechaFestivo, Restaurante restaurante, String createdBy) {
        this.nombre = nombre;
        this.fechaFestivo = fechaFestivo;
        this.restaurante = restaurante;
        this.createdBy = createdBy;
        this.creationDate = LocalDate.now();
    }
}
