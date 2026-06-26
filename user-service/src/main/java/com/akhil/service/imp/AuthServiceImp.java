package com.akhil.service.imp;

import com.akhil.model.User;
import com.akhil.payload.DTO.SignupDto;
import com.akhil.payload.DTO.TokenResponse;
import com.akhil.payload.response.AuthResponse;
import com.akhil.repo.UserRepo;
import com.akhil.service.AuthService;
import com.akhil.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserRepo repo;
    private final KeycloakService keycloakService;

    @Override
    public AuthResponse login(String username, String password) {

        TokenResponse tokenResponse=keycloakService.getAdminAccessToken(username,password,"password",null);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setMessage("Login Successfully");
        return authResponse;
    }

    @Override
    public AuthResponse signup(SignupDto req) {
        keycloakService.createUser(req);
        User user=new User();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setEmail(req.getEmail());
        user.setRole(req.getRole());

        user.setFullName(req.getFirstName()+" "+req.getLastName());
        user.setCreatedAt(LocalDateTime.now());

        repo.save(user);
        TokenResponse tokenResponse=keycloakService.getAdminAccessToken(req.getUsername(),req.getPassword(),"password",null);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setRole(user.getRole());
        authResponse.setMessage("Register Successfully");
        return authResponse;
    }

    @Override
    public AuthResponse getAccessTokenFromRefreshToken(String refreshToken) {
        TokenResponse tokenResponse=keycloakService.getAdminAccessToken(null,null,"refresh_token",refreshToken);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setRefresh_token(tokenResponse.getRefreshToken());
        authResponse.setJwt(tokenResponse.getAccessToken());
        authResponse.setMessage("Login Successfully");
        return authResponse;
    }
}
