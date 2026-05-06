package com.uniquindio.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Actuator: el puerto 8080 no está expuesto por nginx, solo Docker puede llegar
                        .requestMatchers("/actuator/prometheus", "/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        // Endpoints públicos de autenticación
                        .requestMatchers("/api/auth/**").permitAll()
                        // Archivos estáticos de imágenes subidas
                        .requestMatchers("/uploads/**").permitAll()
                        // Lectura de productos y categorías es pública
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                        // Lectura de resenas es publica, escribir requiere cliente autenticado
                        .requestMatchers(HttpMethod.GET, "/api/resenas/producto/*/mi-estado").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/resenas/producto/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/resenas/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/resenas/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/resenas/**").authenticated()
                        // Lectura de imágenes es pública, subida solo para ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/imagenes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/imagenes/**").hasRole("ADMIN")
                        // Escritura de productos y categorías solo para ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasRole("ADMIN")
                        // Pedidos: crear y ver mis pedidos requiere autenticación
                        .requestMatchers(HttpMethod.POST, "/api/pedidos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/mis-pedidos").authenticated()
                        // Pedidos: listar todos y cambiar estado solo ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/pedidos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/*/estado").hasRole("ADMIN")
                        // Pagos: crear preferencia requiere autenticación
                        .requestMatchers(HttpMethod.POST, "/api/pagos/crear").authenticated()
                        // Pagos: webhook es público (Mercado Pago lo llama sin auth)
                        .requestMatchers(HttpMethod.POST, "/api/pagos/webhook").permitAll()
                        // Perfil y direcciones: requiere autenticación
                        .requestMatchers("/api/perfil/**").authenticated()
                        // Endpoints de administración
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Ver factura de pedido requiere autenticación
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/*/factura").authenticated()
                        // Ver detalle de pedido individual requiere autenticación
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/*").authenticated()
                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
