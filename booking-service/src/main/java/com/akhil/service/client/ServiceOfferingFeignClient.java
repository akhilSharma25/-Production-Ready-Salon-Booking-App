package com.akhil.service.client;

import com.akhil.DTO.ServiceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient("SERVICE-OFFERING")
public interface ServiceOfferingFeignClient {
    @GetMapping("/api/service-offering/list/{serviceIds}")
    public ResponseEntity<Set<ServiceDto>> getServicesById(@PathVariable Set<Long> serviceIds);
    }
