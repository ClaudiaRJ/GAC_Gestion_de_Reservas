package com.gac.gac_gestion_de_reservas.restaurantes;

import com.gac.gac_gestion_de_reservas.requests.*;
import com.gac.gac_gestion_de_reservas.response.DiaTrabajoResponse;
import com.gac.gac_gestion_de_reservas.response.VacacionesResponse;
import com.gac.gac_gestion_de_reservas.response.VerEmpleadoResponse;
import com.gac.gac_gestion_de_reservas.response.VerRestauranteResponse;
import com.gac.gac_gestion_de_reservas.restaurantes.empleados.Empleado;
import com.gac.gac_gestion_de_reservas.restaurantes.empleados.EmpleadoRepository;
import com.gac.gac_gestion_de_reservas.restaurantes.empleados.EmpleadoService;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/restaurante")
@AllArgsConstructor
public class RestauranteController {

    private final RestauranteService restauranteService;
    private final EmpleadoService empleadoService;
    private final EmpleadoRepository empleadoRepository;
    private final UsuarioService usuarioService;

    @PostMapping("/registrar-restaurante")
    public ResponseEntity<String> registrarRestaurante(@RequestBody RestauranteRequest request) {
        restauranteService.createRestaurante(request);
        return ResponseEntity.ok("Restaurante registrado!");
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/ver-restaurante")
    public VerRestauranteResponse verRestaurante(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        return restauranteService.verRestauranteById(restaurante.getId());
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/update-restaurante")
    public VerRestauranteResponse updateRestaurante(@AuthenticationPrincipal UserDetails userDetails,@RequestBody UpdateRestauranteRequest request) {
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        return  restauranteService.updateRestauranteById(restaurante.getId(), request);
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/desactivar-restaurante")
    public ResponseEntity<String> desactivarRestaurante(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        restauranteService.deleteRestauranteById(restaurante.getId());
        return ResponseEntity.ok("Restaurante desactivado!");
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/update-gerente-responsable")
    public ResponseEntity<String> updateGerenteResponsable(@AuthenticationPrincipal UserDetails userDetails, UpdateGerenteResponsableRequest request){
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        restauranteService.updateGerenteResponsable(restaurante, request);
        return  ResponseEntity.ok("Gerente responsable actualizado!");
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/set-dias-trabajo")
    public ResponseEntity<String> setDiasTrabajo(@AuthenticationPrincipal UserDetails userDetails,@RequestBody List<RestauranteDiasTrabajoRequest> request ) {
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        restauranteService.setDiaTrabajoRestaurante(restaurante.getId(), request);
        return ResponseEntity.ok("Dia de trabajo Registrado!");
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/get-dias-trabajo")
    public List<DiaTrabajoResponse> getDiaTrabajo(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        return restauranteService.getDiaTrabajoRestaurante(restaurante);
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/set-dias-vacaciones")
    public ResponseEntity<String> setDiasVacaciones(@AuthenticationPrincipal UserDetails userDetails,@RequestBody VacacionesRequest request) {
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        restauranteService.setVacacionesByRestaurante(restaurante.getId(), request);
        return ResponseEntity.ok("Dias de Vacaciones Registrados!") ;
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/get-dias-vacaciones")
    public List<VacacionesResponse> getDiasVacaciones(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        return restauranteService.getVacacionesByRestaurante(restaurante);
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/registrar-empleado")
    public ResponseEntity<String> registrarEmpleado(@RequestBody EmpleadoRequest request) {
        empleadoService.registrarEmpleado(request);
        return ResponseEntity.ok("Empleado registrado exitosamente");
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/listar-empleados-activos")
    public List<VerEmpleadoResponse> listarEmpleados(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        return empleadoService.listarEmpleadosActivoByRestaurante(restaurante) ;
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/listar-todos-empleados")
    public List<VerEmpleadoResponse> listarEmpleadosByRestaurante(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        return empleadoService.listarTodosEmpleadosByRestaurante(restaurante) ;
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/ver-empleado")
    public VerEmpleadoResponse verEmpleado(@RequestBody VerEmpleadoRequest request) {
        return empleadoService.verEmpleado(request.idEmpleado());
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/update-empleado")
    public VerEmpleadoResponse updateEmpleado(@RequestBody EmpleadoUpdateRequest request) {
        return empleadoService.updateEmpleado(request);
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/desactivar-empleado")
    public ResponseEntity<String> desactivarEmpleado(@RequestBody DesactivarEmpleadoRequest request) {
        Empleado empleado = empleadoRepository.findByUsuarioEmail(request.emailEmpleado());
        empleadoService.desactivarEmpleado(empleado);
        return ResponseEntity.ok("Empleado desactivado!");
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/update-rol-empleado")
    public VerEmpleadoResponse updateRolEmpleado(@RequestBody UpdateRolEmpleadoRequest request) {
        return empleadoService.updateRolEmpleado(request);
    }

    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping("/listar-gerente")
    public List<VerEmpleadoResponse> listarGerente(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuarioAutenticado = usuarioService.findUsuarioByEmail(userDetails.getUsername());
        Restaurante restaurante = empleadoRepository.findByUsuario(usuarioAutenticado).getRestaurante();
        return empleadoService.listarGerenteByRestaurante(restaurante);
    }


}
