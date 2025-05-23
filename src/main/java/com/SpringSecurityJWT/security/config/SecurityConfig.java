package com.SpringSecurityJWT.security.config;

import com.SpringSecurityJWT.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AuthenticationProvider authenticationProvider,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {

                    // Rutas públicas
                    auth.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/auth/public-access").permitAll();
                    auth.requestMatchers("/error").permitAll();

                    // Rutas protegidas por permisos
                    auth.requestMatchers(HttpMethod.GET, "/products").hasAuthority("READ_ALL_PRODUCTS");
                    auth.requestMatchers(HttpMethod.POST, "/products").hasAuthority("SAVE_ONE_PRODUCT");

                    // Rutas protegidas por rol
                    auth.requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMINISTRATOR");

                    // Cualquier otra ruta, denegada
                    auth.anyRequest().denyAll();
                });

        return http.build();
    }

}