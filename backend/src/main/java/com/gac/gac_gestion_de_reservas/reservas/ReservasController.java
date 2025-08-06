package com.gac.gac_gestion_de_reservas.reservas;

import com.gac.gac_gestion_de_reservas.requests.ReservasRequest;
import com.gac.gac_gestion_de_reservas.requests.VerReservaRequest;
import com.gac.gac_gestion_de_reservas.response.VerReservaResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/reservas")
@AllArgsConstructor
public class ReservasController {

    private final ReservasService reservasService;

    @PreAuthorize("hasAnyRole('CLIENTE','EMPLEADO','GERENTE')")
    @PostMapping("/crear-reserva")
    public VerReservaResponse crearReserva(@RequestBody ReservasRequest request){
        return reservasService.crearReserva(request);
    }

    @PreAuthorize("hasAnyRole('CLIENTE','EMPLEADO','GERENTE')")
    @PostMapping("/ver-reserva")
    public VerReservaResponse verReserva(@RequestBody VerReservaRequest request){
        return reservasService.verReserva(request.idReserva());
    }

}
