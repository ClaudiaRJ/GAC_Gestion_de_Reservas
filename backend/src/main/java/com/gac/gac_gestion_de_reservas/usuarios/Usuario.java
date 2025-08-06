package com.gac.gac_gestion_de_reservas.usuarios;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "usuario_sequence",
            sequenceName = "usuario_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "usuario_sequence"
    )
    @Column(name = "id", insertable = false, updatable = false)
    private long id;

    @Column(name = "nombre",  columnDefinition = "VARCHAR(20)")
    private String nombre;

    @Column(name = "apellido",  columnDefinition = "VARCHAR(50)")
    private String apellido;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "locked", columnDefinition = "BOOLEAN")
    private Boolean locked = false;

    @Column(name = "enabled", columnDefinition = "BOOLEAN")
    private Boolean enabled = false;

    @Enumerated(EnumType.STRING)
    private RolUsuario rolUsuario;

    public Usuario(String nombre, String apellido, String telefono, String email, String password, RolUsuario rolUsuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.rolUsuario = rolUsuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + rolUsuario.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
