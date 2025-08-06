package com.gac.gac_gestion_de_reservas.restaurantes.empleados;

import com.gac.gac_gestion_de_reservas.restaurantes.Restaurante;
import com.gac.gac_gestion_de_reservas.usuarios.RolUsuario;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Empleado findByUsuario(Usuario usuario);
    Empleado findByUsuarioEmail(String email);
    List<Empleado> findByRestauranteIdAndUsuarioEnabled(long restaurante_id, Boolean usuario_enabled);
    List<Empleado> findByRestauranteId(long restaurante_id);
    List<Empleado> findByUsuario_RolUsuarioAndRestauranteAndUsuarioEnabled(RolUsuario rol, Restaurante restaurante, boolean enabled);
}
