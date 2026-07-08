package com.akhil.service;

import com.akhil.payload.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

    @Value("${keycloak.clientUuid}")
    private String clientUuid;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    private final RestTemplate restTemplate;

    public void createUser(SignupDto signupDto) {

        String accessToken = getAdminAccessToken(username, password, GRANT_TYPE, null).getAccessToken();

        Credential credential = new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(signupDto.getPassword());

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(signupDto.getUsername());
        userRequest.setEmail(signupDto.getEmail());
        userRequest.setEnabled(true);
        userRequest.setLastName(signupDto.getLastName());
        userRequest.setFirstName(signupDto.getFirstName());
        userRequest.setCredentials(List.of(credential)); // FIX: credential ab actually attach ho raha hai

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<UserRequest> requestHttpEntity = new HttpEntity<>(userRequest, httpHeaders);

        try {
            restTemplate.exchange(
                    KEYCLOAK_ADMIN_API,
                    HttpMethod.POST,
                    requestHttpEntity,
                    String.class
            );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("User creation failed: " + e.getResponseBodyAsString(), e);
        }

        System.out.println("User created Successfully");

        KeycloakUserDTO user = fetchFirstUserByUsername(signupDto.getUsername(), accessToken);

        // FIX: ab clientId nahi, hardcoded clientUuid use ho raha hai
        KeycloakRole role = getRoleByName(clientUuid, accessToken, signupDto.getRole().toString());
        List<KeycloakRole> roles = new ArrayList<>();
        roles.add(role);
        assignRoleToUser(user.getId(), clientUuid, roles, accessToken);
    }

    public TokenResponse getAdminAccessToken(String username,
                                             String password,
                                             String grantType,
                                             String refreshToken) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // FIX: JSON nahi, form-urlencoded chahiye

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("username", username);
        requestBody.add("password", password);
        if (refreshToken != null) {                 // FIX: null refresh_token add hi mat karo
            requestBody.add("refresh_token", refreshToken);
        }
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("scope", SCOPE);

        HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    requestHttpEntity,
                    TokenResponse.class
            );

            if (response.getBody() == null) {
                throw new RuntimeException("Failed to obtain access token: empty response body");
            }
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to obtain access token: " + e.getResponseBodyAsString(), e);
        }
    }

    public KeycloakRole getRoleByName(String clientUuid,
                                      String token,
                                      String role) {

        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/clients/" + clientUuid + "/roles/" + role;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> requestHttpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<KeycloakRole> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestHttpEntity,
                    KeycloakRole.class
            );

            if (response.getBody() == null) {
                throw new RuntimeException("Role not found: " + role);
            }
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to fetch role " + role + ": " + e.getResponseBodyAsString(), e);
        }
    }

    public KeycloakUserDTO fetchFirstUserByUsername(String username,
                                                    String token) {

        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/users?username=" + username;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> requestHttpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<KeycloakUserDTO[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestHttpEntity,
                    KeycloakUserDTO[].class
            );

            // FIX: empty array bhi non-null hota hai, length check zaroori
            if (response.getBody() == null || response.getBody().length == 0) {
                throw new RuntimeException("User not found with username " + username);
            }
            return response.getBody()[0];

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to fetch user " + username + ": " + e.getResponseBodyAsString(), e);
        }
    }

    public void assignRoleToUser(String userId, String clientUuid, List<KeycloakRole> roles, String token) {

        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/users/" + userId + "/role-mappings/clients/" + clientUuid;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<List<KeycloakRole>> requestHttpEntity = new HttpEntity<>(roles, httpHeaders);

        try {
            restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestHttpEntity,
                    String.class
            );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to assign role: " + e.getResponseBodyAsString(), e);
        }
    }

    public  KeycloakUserDTO fetchUserProfileByJwt(String token) {
        String url = KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/userinfo";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("Received Token = " + token);

        String rawToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        httpHeaders.setBearerAuth(rawToken);
        System.out.println("rawToken Token = " + rawToken);

        HttpEntity<String> requestHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<KeycloakUserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestHttpEntity,
                    KeycloakUserDTO.class
            );
            return  response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException(
                    "Failed to fetch user info. Status: " + e.getStatusCode() +
                            ", Body: " + e.getResponseBodyAsString(), e
            );        }
    }
    }
