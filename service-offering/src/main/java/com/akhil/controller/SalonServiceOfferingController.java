package com.akhil.controller;

import com.akhil.DTO.CategoryDto;
import com.akhil.DTO.SaloonDTO;
import com.akhil.DTO.ServiceDto;
import com.akhil.model.ServiceOffering;
import com.akhil.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@RestController
@RequestMapping("/api/service-offering/salon-owner")
@RequiredArgsConstructor
public class SalonServiceOfferingController {

    private final ServiceOfferingService service;

    @PostMapping
    public ResponseEntity<ServiceOffering> createService(@RequestBody ServiceDto serviceDto

    ){

        SaloonDTO saloonDTO=new SaloonDTO();
        saloonDTO.setId(1L);

        CategoryDto categoryDto=new CategoryDto();
        categoryDto.setId(serviceDto.getCategoryId());
        ServiceOffering serviceOfferings=service.createService(saloonDTO,serviceDto,categoryDto);
        return ResponseEntity.ok(serviceOfferings);

    }


    @PutMapping("/{serviceId}")
    public ResponseEntity<ServiceOffering> updateService(@PathVariable("serviceId") Long serviceId,@RequestBody ServiceOffering serviceOffering

    ){

        ServiceOffering serviceOfferings=service.updateService(serviceId,serviceOffering);
        return ResponseEntity.ok(serviceOfferings);

    }
}
