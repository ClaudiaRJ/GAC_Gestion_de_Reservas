package com.gac.gac_gestion_de_reservas.reservas;

import com.gac.gac_gestion_de_reservas.restaurantes.Restaurante;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservasRepository extends JpaRepository<Reservas, Long> {

        List<Reservas> findByRestaurante(Restaurante restaurante);
        List<Reservas> findByCliente(Usuario usuario);

}
