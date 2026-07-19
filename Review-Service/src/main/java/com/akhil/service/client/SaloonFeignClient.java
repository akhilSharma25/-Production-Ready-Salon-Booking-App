package com.akhil.service.client;

import com.akhil.DTO.SaloonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("SALOON-SERVICE")
public interface SaloonFeignClient {
    @GetMapping("/api/salons/owner")
    public ResponseEntity<SaloonDTO> getSalonByOwnerId(@RequestHeader("Authorization")String jwt) ;

    @GetMapping("/api/salons/{salonId}")
    public ResponseEntity<SaloonDTO> getSalonById(@PathVariable Long salonId) ;


    }
