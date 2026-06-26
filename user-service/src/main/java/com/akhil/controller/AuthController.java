package com.akhil.controller;

import com.akhil.payload.DTO.LoginDTO;
import com.akhil.payload.DTO.SignupDto;
import com.akhil.payload.response.AuthResponse;
import com.akhil.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupDto signupDto){
        AuthResponse response=service.signup(signupDto);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO){
        AuthResponse response=service.login(loginDTO.getUsername(),loginDTO.getPassword());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/access-token/refresh-token/{refreshToken}")
    public ResponseEntity<AuthResponse> getAccessToken(@PathVariable String refreshToken){
        AuthResponse response=service.getAccessTokenFromRefreshToken(refreshToken);

        return ResponseEntity.ok(response);
    }
}
