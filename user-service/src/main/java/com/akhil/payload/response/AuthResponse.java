package com.akhil.payload.response;

import com.akhil.domain.UserRole;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String refresh_token;
    private String message;
    private UserRole role;
}
