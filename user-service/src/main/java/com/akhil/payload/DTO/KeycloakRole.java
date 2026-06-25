package com.akhil.payload.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class KeycloakRole {

    private String id;
    private String name;
    private String description;
    private boolean enabled;
    private boolean clientRole;
    private String containerId;
    private Map<String,Object> attributes;


}
