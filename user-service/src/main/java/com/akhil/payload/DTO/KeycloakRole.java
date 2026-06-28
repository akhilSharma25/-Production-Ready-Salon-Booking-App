package com.akhil.payload.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class KeycloakRole {

    private String id;
    private String name;
    private String description;
    @JsonIgnore
    private boolean enabled;
    private boolean clientRole;
    private boolean composite;
    private String containerId;
    private Map<String,Object> attributes;


}
