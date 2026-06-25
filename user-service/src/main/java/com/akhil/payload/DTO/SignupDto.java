package com.akhil.payload.DTO;

import com.akhil.domain.UserRole;
import lombok.Data;

@Data
public class SignupDto {

    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String username;
    private UserRole role;
}
