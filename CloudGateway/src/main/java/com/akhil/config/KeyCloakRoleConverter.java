package com.akhil.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KeyCloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // Realm-level roles
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> realmRoles = (List<String>) realmAccess.get("roles");
            realmRoles.forEach(role ->
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
        }

        // Client-level roles
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess != null) {
            resourceAccess.forEach((client, clientDetailsObj) -> {
                if (clientDetailsObj instanceof Map) {
                    Map<String, Object> clientDetails = (Map<String, Object>) clientDetailsObj;
                    if (clientDetails.containsKey("roles")) {
                        List<String> clientRoles = (List<String>) clientDetails.get("roles");
                        clientRoles.forEach(role ->
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
                    }
                }
            });
        }

        return authorities;
    }
}