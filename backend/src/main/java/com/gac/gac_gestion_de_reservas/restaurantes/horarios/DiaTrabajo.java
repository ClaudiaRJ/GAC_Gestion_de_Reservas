package com.gac.gac_gestion_de_reservas.restaurantes.horarios;

import com.gac.gac_gestion_de_reservas.restaurantes.Restaurante;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DiaTrabajo {

    @Id
    @SequenceGenerator(
            name = "dias_trabajo_sequence",
            sequenceName = "dias_trabajo_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "dias_trabajo_sequence"
    )
    private Long id;

    private DayOfWeek dia;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "dia_trabajo_horario",
            joinColumns = @JoinColumn(name = "dia_trabajo_id")
    )
    private List<HorarioTrabajo> horario = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    private String createdBy;
    private LocalDate creationDate;
    private String modifiedBy;
    private LocalDate modificationDate;

    public DiaTrabajo(DayOfWeek dia,
                      List<HorarioTrabajo> horario,
                      Restaurante restaurante,
        String createdBy) {
        this.dia = dia;
        this.horario = horario;
        this.restaurante = restaurante;
        this.createdBy = createdBy;
        this.creationDate = LocalDate.now();
    }
}
