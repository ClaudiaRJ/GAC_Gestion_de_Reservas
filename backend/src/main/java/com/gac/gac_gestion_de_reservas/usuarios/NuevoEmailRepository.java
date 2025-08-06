package com.gac.gac_gestion_de_reservas.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NuevoEmailRepository extends JpaRepository<NuevoEmail, Long>  {

    Optional<NuevoEmail> findByToken(String token);
}
