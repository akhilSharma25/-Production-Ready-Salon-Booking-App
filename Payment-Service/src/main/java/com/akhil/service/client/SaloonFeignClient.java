package com.akhil.service.client;

import com.akhil.payload.DTO.SaloonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("SALOON-SERVICE")
public interface SaloonFeignClient {
    @GetMapping("/api/salons/owner")
    public ResponseEntity<com.akhil.payload.DTO.SaloonDTO> getSalonByOwnerId(@RequestHeader("Authorization")String jwt) ;

    @GetMapping("/api/salons/{salonId}")
    public ResponseEntity<com.akhil.payload.DTO.SaloonDTO> getSalonById(@PathVariable Long salonId) ;


    }
