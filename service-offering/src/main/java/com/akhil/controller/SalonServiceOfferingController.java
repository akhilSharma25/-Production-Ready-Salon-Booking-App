//package com.akhil.controller;
//
//import com.akhil.DTO.CategoryDto;
//import com.akhil.DTO.SaloonDTO;
//import com.akhil.DTO.ServiceDto;
//import com.akhil.model.ServiceOffering;
//import com.akhil.service.ServiceOfferingService;
//import com.akhil.service.client.CategoryFeignClient;
//import com.akhil.service.client.SaloonFeignClient;
//import com.akhil.service.client.UserFeignClient;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Set;
//@RestController
//@RequestMapping("/api/service-offering/salon-owner")
//@RequiredArgsConstructor
//public class SalonServiceOfferingController {
//
//    private final ServiceOfferingService service;
//
//    private final SaloonFeignClient salonFeignClient;
//    private final CategoryFeignClient categoryFeignClient;
//    @PostMapping
//    public ResponseEntity<ServiceOffering> createService(@RequestBody ServiceDto serviceDto,@RequestHeader("Authorization")String jwt
//
//    ){
//
//        SaloonDTO saloonDTO=salonFeignClient.getSalonByOwnerId(jwt).getBody();
//
//        CategoryDto categoryDto=categoryFeignClient.getCategoryByIdAndSalon(serviceDto.getCategoryId(),saloonDTO.getId()).getBody();
//
//        ServiceOffering serviceOfferings=service.createService(saloonDTO,serviceDto,categoryDto);
//        return ResponseEntity.ok(serviceOfferings);
//
//    }
//
//
//    @PutMapping("/{serviceId}")
//    public ResponseEntity<ServiceOffering> updateService(@PathVariable("serviceId") Long serviceId,@RequestBody ServiceOffering serviceOffering
//
//    ){
//
//        ServiceOffering serviceOfferings=service.updateService(serviceId,serviceOffering);
//        return ResponseEntity.ok(serviceOfferings);
//
//    }
//}


package com.akhil.controller;

import com.akhil.DTO.CategoryDto;
import com.akhil.DTO.SaloonDTO;
import com.akhil.DTO.ServiceDto;
import com.akhil.model.ServiceOffering;
import com.akhil.service.ServiceOfferingService;
import com.akhil.service.client.CategoryFeignClient;
import com.akhil.service.client.SaloonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service-offering/salon-owner")
@RequiredArgsConstructor
public class SalonServiceOfferingController {

    private final ServiceOfferingService service;
    private final SaloonFeignClient salonFeignClient;
    private final CategoryFeignClient categoryFeignClient;

    @PostMapping
    public ResponseEntity<ServiceOffering>  createService(@RequestBody ServiceDto serviceDto, @RequestHeader("Authorization") String jwt) {

        SaloonDTO saloonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();

// FIX: Replaced parameter order to match the feign client route mapping safely
        System.out.println("Salon DTO = " + saloonDTO);
        System.out.println("Salon ID = " + saloonDTO.getId());
        System.out.println("Category ID = " + serviceDto.getCategoryId());
        CategoryDto categoryDto =
                (CategoryDto) categoryFeignClient.getCategoryByIdAndSalon(
                        saloonDTO.getId(),
                        serviceDto.getCategoryId()
                ).getBody();
        ServiceOffering serviceOfferings = service.createService(saloonDTO, serviceDto, categoryDto);
        return ResponseEntity.ok(serviceOfferings);
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity updateService(@PathVariable("serviceId") Long serviceId, @RequestBody ServiceOffering serviceOffering) {
        ServiceOffering serviceOfferings = service.updateService(serviceId, serviceOffering);
        return ResponseEntity.ok(serviceOfferings);
    }
}