package com.gac.gac_gestion_de_reservas.restaurantes.empleados;

import com.gac.gac_gestion_de_reservas.email.EmailService;
import com.gac.gac_gestion_de_reservas.requests.EmpleadoRequest;
import com.gac.gac_gestion_de_reservas.requests.EmpleadoUpdateRequest;
import com.gac.gac_gestion_de_reservas.requests.UpdateRolEmpleadoRequest;
import com.gac.gac_gestion_de_reservas.response.VerEmpleadoResponse;
import com.gac.gac_gestion_de_reservas.restaurantes.Restaurante;
import com.gac.gac_gestion_de_reservas.restaurantes.RestauranteService;
import com.gac.gac_gestion_de_reservas.usuarios.RolUsuario;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioRepository;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioService usuarioService;
    private final RestauranteService restauranteService;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;

    public List<VerEmpleadoResponse> listarEmpleadosActivoByRestaurante(Restaurante restaurante) {

        return empleadoRepository.findByRestauranteIdAndUsuarioEnabled(restaurante.getId(), true).stream()
                .map(empleado -> {
                    Usuario u = empleado.getUsuario();
                    return new VerEmpleadoResponse(
                            u.getNombre(),
                            u.getApellido(),
                            u.getTelefono(),
                            u.getEmail(),
                            u.getRolUsuario(),
                            u.isEnabled() ? "Activo" : "Desactivado"
                    );
                })
                .toList();
    }

    public List<VerEmpleadoResponse> listarTodosEmpleadosByRestaurante(Restaurante restaurante) {

        return empleadoRepository.findByRestauranteId(restaurante.getId()).stream()
                .map(empleado -> {
                    Usuario u = empleado.getUsuario();
                    return new VerEmpleadoResponse(
                            u.getNombre(),
                            u.getApellido(),
                            u.getTelefono(),
                            u.getEmail(),
                            u.getRolUsuario(),
                            u.isEnabled() ? "Activo" : "Desactivado"
                    );
                })
                .toList();
    }

    public List<VerEmpleadoResponse> listarGerenteByRestaurante(Restaurante restaurante) {

        return empleadoRepository.findByUsuario_RolUsuarioAndRestauranteAndUsuarioEnabled(RolUsuario.GERENTE,restaurante, true).stream()
                .map(empleado -> {
                    Usuario u = empleado.getUsuario();
                    return new VerEmpleadoResponse(
                            u.getNombre(),
                            u.getApellido(),
                            u.getTelefono(),
                            u.getEmail(),
                            u.getRolUsuario(),
                            u.isEnabled() ? "Activo" : "Desactivado"
                    );
                })
                .toList();
    }

    public void registrarEmpleado(EmpleadoRequest request) {

        Usuario usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        String token = usuarioService.altaUsuario(
             new Usuario(
                     request.nombre(),
                     request.apellido(),
                     request.telefono(),
                     request.email(),
                     "PROVISIONAL",
                     request.rolUsuario()
             )
        );

        if (token.isEmpty()) {
            throw new RuntimeException("No se puede registrar el usuario");
        }
        //TODO::ajustar el email de confirmacion de empleado
        String link = "https://easytable.ddns.net:35800/api/gac/altas/confirm?token=" + token;
        emailService.sendConfirmationToken(request.email(), emailService.emailConfirmationToken(request.nombre(), link));

        Empleado empleado = new Empleado(
                usuarioService.findUsuarioByEmail(request.email()),
                restauranteService.getRestauranteByUsusario(usuarioAutenticado),
                LocalDate.now(),
                usuarioAutenticado.getEmail()
        );
        empleadoRepository.save(empleado);
    }

    public void desactivarEmpleado(Empleado empleado) {
        Usuario usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        if (!empleado.getUsuario().isEnabled()){
            throw new RuntimeException("Usuario ya desactivado");
        }

        Empleado gerente = empleadoRepository.findByUsuario(usuarioAutenticado);

        if (gerente == null) {
            throw new RuntimeException("No se puede eliminar el usuario");
        }

        if (gerente.getRestaurante().getId() != empleado.getRestaurante().getId()) {
            throw new RuntimeException("No se puede eliminar el usuario");
        }

        //desactivo el usuario
        Usuario usuarioEmpleado = empleado.getUsuario();
        usuarioEmpleado.setEnabled(false);
        usuarioRepository.save(usuarioEmpleado);

        //desactivo el empleado
        empleado.setDeletedAt(LocalDate.now());
        empleado.setDeletedBy(usuarioAutenticado.getEmail());
        empleado.setEnabled(false);
        empleadoRepository.save(empleado);
    }

    public VerEmpleadoResponse verEmpleado(Long id) {
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(id);
        if (empleadoOptional.isEmpty()) {
            throw new IllegalStateException("Empleado no encontrado");
        }
        Usuario usuario = empleadoOptional.get().getUsuario();
        if (!usuario.isEnabled()) {
            throw new IllegalStateException("Usuario desactivado!");
        }


        return new VerEmpleadoResponse(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getTelefono(),
                usuario.getEmail(),
                usuario.getRolUsuario(),
                usuario.isEnabled() ? "Activo" : "Desactivado"
        );
    }

    public VerEmpleadoResponse updateRolEmpleado(UpdateRolEmpleadoRequest request) {

        Empleado empleado = empleadoRepository.findByUsuarioEmail(request.email());

        Usuario usuario = empleado.getUsuario();
        if (!usuario.isEnabled()) {
            throw new IllegalStateException("Usuario desactivado!");
        }

        Restaurante restaurante = empleado.getRestaurante();
        
        if (usuario == restaurante.getGerenteResponsable()){
            throw new IllegalStateException("No se puede cambiar el ROL del responsable por un restaurante!");
        }
        
        if(!usuario.getRolUsuario().equals(request.rolUsuario())){
            usuario.setRolUsuario(request.rolUsuario());
        }
        
        usuarioRepository.save(usuario);
        return new VerEmpleadoResponse(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getTelefono(),
                usuario.getEmail(),
                usuario.getRolUsuario(),
                usuario.isEnabled() ? "Activo" : "Desactivado"
        );
    } 

    public VerEmpleadoResponse updateEmpleado(EmpleadoUpdateRequest request) {

        Usuario usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        Optional<Empleado> empleadoOptional = empleadoRepository.findById(request.idEmpleado());


        if (empleadoOptional.isEmpty()) {
            throw new IllegalStateException("Empleado no encontrado");
        }

        Empleado gerente = empleadoRepository.findByUsuario(usuarioAutenticado);
        Empleado empleado = empleadoOptional.get();

        if (!empleado.getUsuario().isEnabled()) {
            throw new IllegalStateException("Usuario desactivado!");
        }


        if (gerente.getRestaurante().getId() != empleado.getRestaurante().getId()) {
            throw new IllegalStateException("Error al actualizar el empleado");
        }

        Usuario usuario = empleadoOptional.get().getUsuario();

        if (!usuario.getNombre().equals(request.nombre())) {
            usuario.setNombre(request.nombre());
        }
        if (!usuario.getApellido().equals(request.apellido())) {
            usuario.setApellido(request.apellido());
        }
        if (!usuario.getTelefono().equals(request.telefono())) {
            usuario.setTelefono(request.telefono());
        }

        usuarioRepository.save(usuario);

        return new VerEmpleadoResponse(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getTelefono(),
                usuario.getEmail(),
                usuario.getRolUsuario(),
                usuario.isEnabled() ? "Activo" : "Desactivado"
        );
    }
}
