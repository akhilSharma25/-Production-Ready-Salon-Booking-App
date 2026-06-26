package com.akhil.service;

import com.akhil.payload.DTO.SignupDto;
import com.akhil.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse login(String username,String password);
    AuthResponse signup(SignupDto req);
    AuthResponse getAccessTokenFromRefreshToken(String refreshToken);
}
