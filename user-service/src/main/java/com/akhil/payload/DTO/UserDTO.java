package com.akhil.payload.DTO;

import com.akhil.domain.UserRole;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String fullName;
    private String email;
    private UserRole role;

}
