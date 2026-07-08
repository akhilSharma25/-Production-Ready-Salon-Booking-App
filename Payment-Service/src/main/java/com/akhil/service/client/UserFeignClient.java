package com.akhil.service.client;

import com.akhil.payload.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("USER-SERVICE")
public interface UserFeignClient {

    @GetMapping("/api/users/profile")
    public ResponseEntity<com.akhil.payload.DTO.UserDTO> getUserProfile(@RequestHeader("Authorization") String jwt);

    @GetMapping("/api/users/{id}")
    public ResponseEntity<com.akhil.payload.DTO.UserDTO> getUserById(@PathVariable Long id) ;
}
