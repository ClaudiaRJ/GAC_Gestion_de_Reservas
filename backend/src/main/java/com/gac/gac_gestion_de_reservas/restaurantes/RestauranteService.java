package com.gac.gac_gestion_de_reservas.restaurantes;

import com.gac.gac_gestion_de_reservas.requests.*;
import com.gac.gac_gestion_de_reservas.response.DiaTrabajoResponse;
import com.gac.gac_gestion_de_reservas.response.VacacionesResponse;
import com.gac.gac_gestion_de_reservas.response.VerRestauranteResponse;
import com.gac.gac_gestion_de_reservas.restaurantes.empleados.Empleado;
import com.gac.gac_gestion_de_reservas.restaurantes.empleados.EmpleadoRepository;
import com.gac.gac_gestion_de_reservas.restaurantes.horarios.DiaTrabajo;
import com.gac.gac_gestion_de_reservas.restaurantes.horarios.DiasTrabajoRepository;
import com.gac.gac_gestion_de_reservas.restaurantes.horarios.Vacaciones;
import com.gac.gac_gestion_de_reservas.restaurantes.horarios.VacacionesRepository;
import com.gac.gac_gestion_de_reservas.usuarios.RolUsuario;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioRepository;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioService;
import com.gac.gac_gestion_de_reservas.usuarios.altas.SolicitudAltaUsuario;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioService usuarioService;
    private final EmpleadoRepository empleadoRepository;
    private final DiasTrabajoRepository diasTrabajoRepository;
    private final VacacionesRepository vacacionesRepository;
    private final UsuarioRepository usuarioRepository;

    public Restaurante getRestauranteByUsusario(Usuario ususario) {
        return restauranteRepository.findByGerenteResponsable(ususario);
    }

    public void createRestaurante(RestauranteRequest restauranteRequest) {

        Optional<Restaurante> restauranteOptional = restauranteRepository.findByCIFRestaurante(restauranteRequest.CIFRestaurante());
        if (restauranteOptional.isPresent()) {
            throw new IllegalStateException("Ya existe un restaurante registrado con este CIF!");
        }

        SolicitudAltaUsuario solicitudAltaUsuario =
                new SolicitudAltaUsuario(
                        restauranteRequest.nombreGerente(),
                        restauranteRequest.apellidoGerente(),
                        restauranteRequest.telefonoGerente(),
                        restauranteRequest.emailGerente(),
                        restauranteRequest.passwordGerente(),
                        RolUsuario.GERENTE);
        Usuario gerente = usuarioService.altaUsuarioGerente(solicitudAltaUsuario);

        if (gerente == null) {
            throw new IllegalStateException("Error al registrar el usuario!");
        }

        Restaurante restaurante = new Restaurante(
                restauranteRequest.nombre(),
                restauranteRequest.CIFRestaurante(),
                restauranteRequest.descripcionRestaurante(),
                restauranteRequest.telefonoRestaurante(),
                restauranteRequest.direccionRestaurante(),
                restauranteRequest.provinciaRestaurante(),
                restauranteRequest.ciudadRestaurante(),
                restauranteRequest.emailRestaurante(),
                gerente
        );

        restauranteRepository.save(restaurante);
        Empleado empleado =
                new Empleado(
                        gerente,
                        restaurante,
                        LocalDate.now(),
                        restaurante.getEmailRestaurante());
        empleadoRepository.save(empleado);
    }

    public VerRestauranteResponse verRestauranteById(Long id) {

        Restaurante restauranteExistente = verificaRestaurante(id);

        return
                new VerRestauranteResponse(
                        restauranteExistente.getId(),
                        restauranteExistente.getCIFRestaurante(),
                        restauranteExistente.getNombreRestaurante(),
                        restauranteExistente.getDescripcionRestaurante(),
                        restauranteExistente.getDireccionRestaurante(),
                        restauranteExistente.getProvinciaRestaurante(),
                        restauranteExistente.getCiudadRestaurante(),
                        restauranteExistente.getTelefonoRestaurante(),
                        restauranteExistente.getEmailRestaurante(),
                        restauranteExistente.getGerenteResponsable().getEmail());
    }

    public VerRestauranteResponse updateRestauranteById(Long id, UpdateRestauranteRequest request) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(id);
        if (restauranteOptional.isEmpty()) {
            throw new IllegalStateException("Restaurante no encontrado!");
        }

        if (!restauranteOptional.get().isEnabled()) {
            throw new IllegalStateException("Restaurante desactivado!");
        }

        Restaurante restauranteExistente = restauranteOptional.get();

        if (!request.cif().equals(restauranteExistente.getCIFRestaurante())) {
            restauranteExistente.setCIFRestaurante(request.cif());
        }
        if (!request.nombre().equals(restauranteExistente.getNombreRestaurante())) {
            restauranteExistente.setNombreRestaurante(request.nombre());
        }
        if (!request.descripcion().equals(restauranteExistente.getDescripcionRestaurante())) {
            restauranteExistente.setDescripcionRestaurante(request.descripcion());
        }
        if (!request.direccion().equals(restauranteExistente.getDireccionRestaurante())) {
            restauranteExistente.setDireccionRestaurante(request.direccion());
        }
        if (!request.provincia().equals(restauranteExistente.getProvinciaRestaurante())) {
            restauranteExistente.setProvinciaRestaurante(request.provincia());
        }
        if (!request.ciudad().equals(restauranteExistente.getCiudadRestaurante())) {
            restauranteExistente.setCiudadRestaurante(request.ciudad());
        }
        if (!request.telefono().equals(restauranteExistente.getTelefonoRestaurante())) {
            restauranteExistente.setTelefonoRestaurante(request.telefono());
        }
        if (!request.email().equals(restauranteExistente.getEmailRestaurante())) {
            restauranteExistente.setEmailRestaurante(request.email());
        }

        Usuario usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        restauranteExistente.setUpdatedBy(usuarioAutenticado.getEmail());
        restauranteExistente.setUpdatedAt(LocalDate.now());

        restauranteRepository.save(restauranteExistente);

        return
                new VerRestauranteResponse(
                        restauranteExistente.getId(),
                        restauranteExistente.getCIFRestaurante(),
                        restauranteExistente.getNombreRestaurante(),
                        restauranteExistente.getDescripcionRestaurante(),
                        restauranteExistente.getDireccionRestaurante(),
                        restauranteExistente.getProvinciaRestaurante(),
                        restauranteExistente.getCiudadRestaurante(),
                        restauranteExistente.getTelefonoRestaurante(),
                        restauranteExistente.getEmailRestaurante(),
                        restauranteExistente.getGerenteResponsable().getEmail());
    }

    public void deleteRestauranteById(Long id) {

        Restaurante restauranteExistente  = verificaRestaurante(id);
        restauranteExistente.setEnabled(false);

        Usuario usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        restauranteExistente.setDisabledBy(usuarioAutenticado.getEmail());
        restauranteExistente.setDisabledAt(LocalDate.now());

        restauranteRepository.save(restauranteExistente);

    }

    private Restaurante verificaRestaurante(Long id) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(id);
        if (restauranteOptional.isEmpty()) {
            throw new IllegalStateException("Restaurante no encontrado!");
        }

        if (!restauranteOptional.get().isEnabled()) {
            throw new IllegalStateException("Restaurante desactivado!");
        }

        return restauranteOptional.get();
    }

    @Transactional
    public void setDiaTrabajoRestaurante(Long id, List<RestauranteDiasTrabajoRequest> request){
        Usuario usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        Restaurante restaurante = verificaRestaurante(id);

        List<DiaTrabajo> diasTrabajo = request.stream().map(req -> new DiaTrabajo(
                req.dia(),
                req.horarioTrabajos(),
                restaurante,
                usuarioAutenticado.getEmail()
        )
        ).toList();

        diasTrabajoRepository.saveAll(diasTrabajo);

    }

    @Transactional
    public List<DiaTrabajoResponse> getDiaTrabajoRestaurante(Restaurante restaurante){

        List<DiaTrabajo> diasTrabajo = diasTrabajoRepository.findAllByRestaurante(restaurante);

        return diasTrabajo.stream().
                map(diaTrabajo -> new DiaTrabajoResponse(
                        diaTrabajo.getDia(),
                        diaTrabajo.getHorario()
                ))
                .toList();
    }

    public void setVacacionesByRestaurante(Long id, VacacionesRequest request){
            Usuario usuarioAutenticado = usuarioService.getUsuarioAutenticado();
            Restaurante restaurante = verificaRestaurante(id);

            System.out.println(request.comienzoVacaciones() + " " + request.finalVacaciones());


            if (request.finalVacaciones().isBefore(request.comienzoVacaciones())){
                throw new IllegalStateException("El final de las vacaciones no pueden ser antes del comienzo!");
            }

            vacacionesRepository.save(new Vacaciones(
                    request.comienzoVacaciones(),
                    request.finalVacaciones(),
                    restaurante,
                    usuarioAutenticado.getEmail()));
    }

    public List<VacacionesResponse> getVacacionesByRestaurante(Restaurante restaurante){

            List<Vacaciones> vacacionesRestaurante = vacacionesRepository.findByRestaurante(restaurante);

            return vacacionesRestaurante.stream().map(diasVacaciones -> new VacacionesResponse(
                    diasVacaciones.getComienzoVacaciones(),
                    diasVacaciones.getFinalVacaciones()
            )).toList();
    }

    public void updateGerenteResponsable(Restaurante restaurante, UpdateGerenteResponsableRequest request) {
        Usuario usuario = usuarioRepository.findUsuarioByEmail(request.email());

        if (usuario == null) {
            throw new IllegalStateException("Usuario no encontrado!");
        }

        restaurante.setGerenteResponsable(usuario);
        restauranteRepository.save(restaurante);
    }
}