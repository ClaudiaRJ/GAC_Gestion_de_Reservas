package com.gac.gac_gestion_de_reservas.restaurantes;

import com.gac.gac_gestion_de_reservas.restaurantes.horarios.DiaTrabajo;
import com.gac.gac_gestion_de_reservas.restaurantes.horarios.Vacaciones;
import com.gac.gac_gestion_de_reservas.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurante {


    @Id
    @SequenceGenerator(
            name = "restaurante_sequence",
            sequenceName = "restaurante_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "restaurante_sequence"
    )
    private long id;
    private String nombreRestaurante;
    private String CIFRestaurante;
    private String descripcionRestaurante;
    private String telefonoRestaurante;
    private String direccionRestaurante;
    private String provinciaRestaurante;
    private String ciudadRestaurante;
    private String emailRestaurante;
    private int aforo;
    private boolean enabled = true;
    private boolean suscrito = false;
    private String updatedBy;
    private LocalDate updatedAt;
    private String disabledBy;
    private LocalDate disabledAt;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario gerenteResponsable;
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaTrabajo> horarios;
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacaciones> vacaciones;

    //private List<String> imagenRestaurante;

    public Restaurante(String nombreRestaurante,String CIFRestaurante, String descripcionRestaurante, String telefonoRestaurante, String direccionRestaurante, String provinciaRestaurante, String ciudadRestaurante, String emailRestaurante, Usuario gerenteResponsable) {
        this.nombreRestaurante = nombreRestaurante;
        this.CIFRestaurante = CIFRestaurante;
        this.descripcionRestaurante = descripcionRestaurante;
        this.telefonoRestaurante = telefonoRestaurante;
        this.direccionRestaurante = direccionRestaurante;
        this.provinciaRestaurante = provinciaRestaurante;
        this.ciudadRestaurante = ciudadRestaurante;
        this.emailRestaurante = emailRestaurante;
        this.gerenteResponsable = gerenteResponsable;
    }
}
