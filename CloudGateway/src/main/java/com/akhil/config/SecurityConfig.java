//package com.akhil.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.cors.reactive.CorsConfigurationSource;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//import reactor.core.publisher.Mono;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {UrlBasedCorsConfigurationSource
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity){
//
//        httpSecurity.authorizeExchange(exchange -> exchange
//                .pathMatchers("/auth/**").permitAll()
//                .pathMatchers("/api/notifications/**").permitAll()
//
//                // Specific (salon-owner only) FIRST
//                .pathMatchers(
//                        "/api/categories/salon-owner/**",
//                        "/api/notifications/salon-owner/**",
//                        "/api/service-offering/salon-owner/**"
//                ).hasAnyRole("SALON_OWNER")
//
//                // Broad (general access) AFTER
//                .pathMatchers(
//                        "/api/salons/**", "/api/categories/**", "/api/notifications/**",
//                        "/api/bookings/**", "/api/payments/**", "/api/service-offerings/**",
//                        "/api/users/**", "/api/reviews/**"
//                ).hasAnyRole("CUSTOMER", "SALON_OWNER", "ADMIN")
//
//                .anyExchange().authenticated()
//
//        )   .oauth2ResourceServer(oauth2 -> oauth2
//                .jwt(jwt -> jwt.jwtAuthenticationConverter(grantAuthoritiesExtractor()))
//        );
//httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
//        .cors(cors->cors.configurationSource(corsConfigurationSource()));
//
//        return httpSecurity.build();
//    }
//
//    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfigurationSource configuration=new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:5170"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT","OPTIONS","PATCH"));
//        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
//
//      UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**",configuration);
//        return source;
//    }
//
//    private Converter<Jwt,? extends Mono<? extends AbstractAuthenticationToken>> grantAuthoritiesExtractor() {
//
//        JwtAuthenticationConverter jwtAuthenticationConverter=new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
//                new KeyCloakRoleConverter()
//        );
//
//        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
//    }
//
//}


package com.akhil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration; // Yeh import zaroori hai
import org.springframework.web.cors.reactive.CorsConfigurationSource; // Interface import
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource; // Implementation import
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig { // FIX 1: Yahan se floating 'UrlBasedCorsConfigurationSource' text hata diya hai

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity){

        httpSecurity.authorizeExchange(exchange -> exchange
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers("/api/notifications/**").permitAll()

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

        // Ab yeh line koi error nahi degi
        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return httpSecurity.build();
    }

    // FIX 2: Return type ko 'CorsConfigurationSource' interface banaya taaki Spring ise accept kare
    private CorsConfigurationSource corsConfigurationSource() {

        // FIX 3: Variable type ko 'CorsConfiguration' kiya (bina 'Source' ke)
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5170"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> grantAuthoritiesExtractor() {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                new KeyCloakRoleConverter()
        );

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
