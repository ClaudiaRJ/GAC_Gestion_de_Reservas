package com.gac.gac_gestion_de_reservas.restaurantes.horarios;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class HorarioTrabajo {

    private LocalTime horaApertura;
    private LocalTime horaCierre;

}
