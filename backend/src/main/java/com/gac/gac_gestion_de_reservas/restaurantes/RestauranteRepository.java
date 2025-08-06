package com.gac.gac_gestion_de_reservas.restaurantes;

import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    Restaurante findByGerenteResponsable(Usuario usuario);
    Optional<Restaurante> findByCIFRestaurante(String CIFRestaurante);

}
