package com.akhil.service.client;

import com.akhil.payload.DTO.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CATEGORY-SERVICE")
public interface CategoryFeignClient {
    @GetMapping("/api/categories/{id}")
    public ResponseEntity<com.akhil.payload.DTO.CategoryDto> getCategoryById(@PathVariable("id") Long id);
    @GetMapping("/api/categories/salon-owner/salon/{salonId}/category/{id}")
    public ResponseEntity<com.akhil.payload.DTO.CategoryDto
            > getCategoryByIdAndSalon(@PathVariable Long id, @PathVariable Long salonId);
}
