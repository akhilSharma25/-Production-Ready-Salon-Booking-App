        package com.akhil.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String KEYCLOAK_BASE_URL = "http://localhost:8080";
    private static final String KEYCLOAK_ADMIN_API =
            KEYCLOAK_BASE_URL + "/admin/realms/master/users";

    private static final String TOKEN_URL =
            KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/token";

    private static final String GRANT_TYPE = "password";
    private static final String SCOPE = "openid profile email";

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;
}
