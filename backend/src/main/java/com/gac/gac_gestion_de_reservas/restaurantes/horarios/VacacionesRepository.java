package com.gac.gac_gestion_de_reservas.restaurantes.horarios;

import com.gac.gac_gestion_de_reservas.restaurantes.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacacionesRepository extends JpaRepository<Vacaciones, Long> {
    List<Vacaciones> findByRestaurante(Restaurante restaurante);
}
