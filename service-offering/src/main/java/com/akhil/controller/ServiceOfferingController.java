package com.akhil.controller;

import com.akhil.model.ServiceOffering;
import com.akhil.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/service-offering")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final ServiceOfferingService service;


    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Set<ServiceOffering>> getServicesBySalonId(@PathVariable Long salonId, @RequestParam(required = false) Long categoryId){

        Set<ServiceOffering> serviceOfferings=service.getAllServiceBySalon(salonId,categoryId);
        return ResponseEntity.ok(serviceOfferings);

    }


    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceOffering> getServiceById(@PathVariable Long serviceId){

        ServiceOffering serviceOfferings=service.getServiceById(serviceId);
        return ResponseEntity.ok(serviceOfferings);

    }

    @GetMapping("/list/{serviceIds}")
    public ResponseEntity<Set<ServiceOffering>> getServicesById( @PathVariable Set<Long> serviceIds){

        Set<ServiceOffering> serviceOfferings=service.getServicesByIds(serviceIds);
        return ResponseEntity.ok(serviceOfferings);
    }

}
