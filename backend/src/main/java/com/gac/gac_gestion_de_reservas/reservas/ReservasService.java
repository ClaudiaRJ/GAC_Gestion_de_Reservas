package com.gac.gac_gestion_de_reservas.reservas;

import com.gac.gac_gestion_de_reservas.requests.ReservasRequest;
import com.gac.gac_gestion_de_reservas.response.VerReservaResponse;
import com.gac.gac_gestion_de_reservas.restaurantes.Restaurante;
import com.gac.gac_gestion_de_reservas.restaurantes.RestauranteRepository;
import com.gac.gac_gestion_de_reservas.restaurantes.RestauranteService;
import com.gac.gac_gestion_de_reservas.restaurantes.empleados.Empleado;
import com.gac.gac_gestion_de_reservas.restaurantes.empleados.EmpleadoRepository;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioRepository;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservasService {

    private final ReservasRepository reservasRepository;
    private final UsuarioService usuarioService;
    private final RestauranteService restauranteService;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository;

    public VerReservaResponse crearReserva(ReservasRequest request){
        Usuario usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(request.restauranteId());
        if(restauranteOptional.isEmpty()){
            throw new IllegalStateException("Restaurante no encontrado!");
        }
        Restaurante restauranteReserva = restauranteOptional.get();

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(request.clienteId());
        if(usuarioOptional.isEmpty()){
            throw new IllegalStateException("Usuario no encontrado!");
        }
        Usuario clienteReserva = usuarioOptional.get();

        Reservas newReserva =
                new Reservas(restauranteReserva,
                        clienteReserva,
                        request.fecha(),
                        request.hora(),
                        request.personasReserva(),
                        usuarioAutenticado.getEmail());
        reservasRepository.save(newReserva);

        return
                new VerReservaResponse(
                        restauranteService.verRestauranteById(newReserva.getRestaurante().getId()),
                        usuarioService.verUsuarioByEmail(newReserva.getCliente().getEmail()),
                        newReserva.getFecha(),
                        newReserva.getHora(),
                        newReserva.getEstadoReserva()
                        );
    }

    public VerReservaResponse verReserva(Long idReserva){
        Optional<Reservas> reservaProvisional = reservasRepository.findById(idReserva);
        if (reservaProvisional.isEmpty()){
            throw new IllegalStateException("Reserva no encontrada");
        }
        Reservas reserva = reservaProvisional.get();

        Usuario usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        boolean esCliente = usuarioAutenticado.getEmail().equals(reserva.getCliente().getEmail());

        Empleado empleado = empleadoRepository.findByUsuario(usuarioAutenticado);
        boolean esGerenteDelRestaurante = empleado != null &&
                empleado.getRestaurante().getCIFRestaurante().equals(reserva.getRestaurante().getCIFRestaurante());

        if (!(esCliente || esGerenteDelRestaurante)) {
            throw new IllegalStateException("Acceso no permitido");
        }

        return
                new VerReservaResponse(
                        restauranteService.verRestauranteById(reserva.getRestaurante().getId()),
                        usuarioService.verUsuarioByEmail(reserva.getCliente().getEmail()),
                        reserva.getFecha(),
                        reserva.getHora(),
                        reserva.getEstadoReserva());
    }

}
