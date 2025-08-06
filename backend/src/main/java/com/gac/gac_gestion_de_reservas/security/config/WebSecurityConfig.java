package com.gac.gac_gestion_de_reservas.security.config;

import com.gac.gac_gestion_de_reservas.jwt.JWTAuthenticationFilter;
import com.gac.gac_gestion_de_reservas.jwt.JWTUtil;
import com.gac.gac_gestion_de_reservas.usuarios.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UsuarioService usuarioService;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/gac/auth/**",
                                "/api/gac/altas/**",
                                "/api/restaurante/registrar-restaurante").permitAll()

                        .requestMatchers(
                                "/api/gac/usuario/update-email",
                                "/api/gac/usuario/update-password",
                                "/api/gac/usuario/update-usuario",
                                "/api/gac/usuario/confirm-update-email",
                                "/api/restaurante/ver-restaurante",
                                "/api/restaurante/update-restaurante",
                                "/api/restaurante/desactivar-restaurante",
                                "/api/restaurante/update-gerente-responsable",
                                "/api/restaurante/set-dias-trabajo",
                                "/api/restaurante/get-dias-trabajo",
                                "api/restaurante/set-dias-vacaciones",
                                "/api/restaurante/get-dias-vacaciones",
                                "/api/restaurante/registrar-empleado",
                                "/api/restaurante/listar-empleados-activos",
                                "/api/restaurante/listar-todos-empleados",
                                "api/restaurante/ver-empleado",
                                "/api/restaurante/update-empleado",
                                "/api/restaurante/desactivar-empleado",
                                "/api/restaurante/update-rol-empleado",
                                "/api/restaurante/listar-gerente",
                                "/api/reservas/crear-reserva",
                                "/api/reservas/ver-reserva").authenticated()
                        .anyRequest().denyAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);

        return authManagerBuilder.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthFilter() {
        return new JWTAuthenticationFilter(jwtUtil, usuarioService);
    }



    @Bean
    public UserDetailsService userDetailsService() {
        return usuarioService;
    }
}
