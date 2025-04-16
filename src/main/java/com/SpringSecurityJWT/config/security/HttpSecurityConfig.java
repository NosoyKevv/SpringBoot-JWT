package com.SpringSecurityJWT.config.security;

import com.SpringSecurityJWT.utils.Permission;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    public HttpSecurityConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionMangConfig -> sessionMangConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(authConfig -> {

                    //PUBLICAS
                    authConfig.requestMatchers(HttpMethod.POST, ("/auth/authenticate")).permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, ("/auth/public-access")).permitAll();
                    authConfig.requestMatchers("/error").permitAll();

                    //PRIVADAS
                    authConfig.requestMatchers(HttpMethod.GET, "/products").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
                    authConfig.requestMatchers(HttpMethod.POST, "/products").hasAuthority(Permission.SAVE_ONE_PRODUCT.name());

                    //DENEGADO si no esta mapeado arriba no permita ninguno.
                    authConfig.anyRequest().denyAll();

                });

        return http.build();
    }
}
