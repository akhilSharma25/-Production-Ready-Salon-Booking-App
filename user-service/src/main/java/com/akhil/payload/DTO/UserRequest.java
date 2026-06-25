package com.akhil.payload.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private boolean enabled;
    private String email;
    private String username;
    private List<Credential> credentials=new ArrayList<>();
}
