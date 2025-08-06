package com.gac.gac_gestion_de_reservas.restaurantes.horarios;

import com.gac.gac_gestion_de_reservas.restaurantes.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiasTrabajoRepository extends JpaRepository<DiaTrabajo, Long> {
    List<DiaTrabajo> findAllByRestaurante(Restaurante restaurante);
}
