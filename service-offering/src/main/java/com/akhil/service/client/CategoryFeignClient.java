package com.akhil.service.client;

import com.akhil.DTO.CategoryDto;
import com.akhil.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("CATEGORY-SERVICE")
public interface CategoryFeignClient {
    @GetMapping("/api/categories/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") Long id);
    @GetMapping("/api/categories/salon-owner/salon/{salonId}/category/{id}")
    public ResponseEntity<CategoryDto> getCategoryByIdAndSalon(@PathVariable Long id,@PathVariable Long salonId);
}
