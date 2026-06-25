        package com.akhil.service;

import com.akhil.payload.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;


    @Autowired
    private final RestTemplate restTemplate;

    public  void createUser(SignupDto signupDto) throws  RuntimeException{

        String ACCESS_TOKEN=getAdminAccessToken(username,password,GRANT_TYPE,null).getAccessToken();
        Credential credential=new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(signupDto.getPassword());

        UserRequest userRequest=new UserRequest();
        userRequest.setUsername(signupDto.getUsername());
        userRequest.setEmail(signupDto.getEmail());
        userRequest.setEnabled(true);
        userRequest.setLastName(signupDto.getLastName());
        userRequest.setFirstName(signupDto.getFirstName());


        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(ACCESS_TOKEN);

        HttpEntity<UserRequest>requestHttpEntity=new HttpEntity<>(userRequest,httpHeaders);
        ResponseEntity<String> response=restTemplate.exchange(
                KEYCLOAK_ADMIN_API,
                HttpMethod.POST,
                requestHttpEntity
                ,String.class
        );

        if(response.getStatusCode()==HttpStatus.CREATED){
            System.out.println("User created Successfully");
            KeycloakUserDTO user=fetchFirstUserByUsername(signupDto.getUsername(), ACCESS_TOKEN);
            KeycloakRole role=getRoleByName(clientId,ACCESS_TOKEN,signupDto.getRole().toString());
            List<KeycloakRole>roles=new ArrayList<>();
            roles.add(role);
            assignRoleToUser(user.getId(),clientId,roles,ACCESS_TOKEN);

        }else{
            System.out.println("User creation failed");
            throw new RuntimeException(response.getBody());
        }
    }


    public TokenResponse getAdminAccessToken(String username,
                                             String password,
                                             String grantType,
                                             String refreshToken){

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String,String> requestBody=new LinkedMultiValueMap<>();
        requestBody.add("grant_type",grantType);
        requestBody.add("username",username);
        requestBody.add("password",password);
        requestBody.add("refresh_token",refreshToken);
        requestBody.add("client_id",clientId);
        requestBody.add("client_secret",clientSecret);
        requestBody.add("scope",scope);

        HttpEntity<MultiValueMap<String,String> >requestHttpEntity=new HttpEntity<>(requestBody,httpHeaders);
        ResponseEntity<TokenResponse> response=restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                requestHttpEntity
                ,TokenResponse.class
        );

        if(response.getStatusCode()==HttpStatus.OK && response.getBody()!=null){
            return response.getBody();

        }else{
            throw new RuntimeException("Failed to obtain access token");

        }

    }

    public KeycloakRole getRoleByName(String clientId,
                                      String token,
                                      String role){

        String url=KEYCLOAK_BASE_URL+"/admin/realms/master/clients/"+clientId+"/roles/"+role;
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
         httpHeaders.set("Authorization","Bearer "+token);


        HttpEntity<Void>requestHttpEntity=new HttpEntity<>(httpHeaders);
        ResponseEntity<KeycloakRole> response=restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestHttpEntity
                ,KeycloakRole.class
        );

        if(response.getStatusCode()==HttpStatus.OK && response.getBody()!=null){
            return response.getBody();

        }else{
            throw new RuntimeException("Failed to obtain access token");

        }
    }

    public KeycloakUserDTO fetchFirstUserByUsername(String username,
                                                    String token){

        String url=KEYCLOAK_BASE_URL+"/admin/realms/master/users?username="+username;
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);


        HttpEntity<String>requestHttpEntity=new HttpEntity<>(httpHeaders);
        ResponseEntity<KeycloakUserDTO[]> response=restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestHttpEntity
                ,KeycloakUserDTO[].class
        );

        if(response.getStatusCode()==HttpStatus.OK && response.getBody()!=null){
            return response.getBody()[0];

        }else{
            throw new RuntimeException("user not found with username "+username);

        }

    }

    public void assignRoleToUser(String userId, String clientId, List<KeycloakRole> roles,String token){


        String url=KEYCLOAK_BASE_URL+"/admin/realms/master/users/"+userId+"/role-mappings/clients/"+clientId;
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);


        HttpEntity<List<KeycloakRole>>requestHttpEntity=new HttpEntity<>(roles,httpHeaders);


        try{
            ResponseEntity<String> response=restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestHttpEntity
                    ,String.class
            );
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to assign new Role"+e.getMessage());
        }

    }


}
