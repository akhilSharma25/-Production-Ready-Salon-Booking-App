package com.akhil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity){

        httpSecurity.authorizeExchange(exchange -> exchange
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers("/api/notifications/ws/**").permitAll()

                // Specific (salon-owner only) FIRST
                .pathMatchers(
                        "/api/categories/salon-owner/**",
                        "/api/notifications/salon-owner/**",
                        "/api/service-offering/salon-owner/**"
                ).hasAnyRole("SALON_OWNER")

                // Broad (general access) AFTER
                .pathMatchers(
                        "/api/salons/**", "/api/categories/**", "/api/notifications/**",
                        "/api/bookings/**", "/api/payments/**", "/api/service-offerings/**",
                        "/api/users/**", "/api/reviews/**"
                ).hasAnyRole("CUSTOMER", "SALON_OWNER", "ADMIN")

                .anyExchange().authenticated()

        )   .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(grantAuthoritiesExtractor()))
        );
httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);

        return httpSecurity.build();
    }

    private Converter<Jwt,? extends Mono<? extends AbstractAuthenticationToken>> grantAuthoritiesExtractor() {

        JwtAuthenticationConverter jwtAuthenticationConverter=new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                new KeyCloakRoleConverter()
        );

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}
