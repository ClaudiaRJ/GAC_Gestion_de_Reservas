package com.gac.gac_gestion_de_reservas.reservas;

import com.gac.gac_gestion_de_reservas.restaurantes.Restaurante;
import com.gac.gac_gestion_de_reservas.restaurantes.empleados.Empleado;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Reservas {
    @Id
    @SequenceGenerator(
            name = "reservas_sequence",
            sequenceName = "reservas_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reservas_sequence"
    )
    private long id;
    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario cliente;
    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado responsableReserva;
    private LocalDate fecha;
    private LocalTime hora;
    private int personasReserva;
    @Enumerated(EnumType.STRING)
    private EstadoReserva estadoReserva;
    private String createdBy;
    private LocalDate createdAt;
    private String lastModifiedBy;
    private LocalDate lastModifiedAt;

    public Reservas(Restaurante restaurante, Usuario cliente, LocalDate fecha, LocalTime hora, int personasReserva, String createdBy){
        this.restaurante = restaurante;
        this.cliente = cliente;
        this.fecha = fecha;
        this.hora = hora;
        this.personasReserva = personasReserva;
        this.createdBy = createdBy;
        this.createdAt = LocalDate.now();
        this.estadoReserva = EstadoReserva.PENDIENTE;
    }

}
