package com.akhil.controller;

import com.akhil.DTO.SaloonDTO;
import com.akhil.model.Category;
import com.akhil.service.CategoryService;
import com.akhil.service.client.SaloonFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/categories/salon-owner")
public class SalonCategoryController {

    @Autowired
    private CategoryService service;

    @Autowired
    private SaloonFeignClient saloonFeignClient;

    @PostMapping()
    public ResponseEntity<Category> createCategory(@RequestBody Category category,@RequestHeader("Authorization")String jwt){

        SaloonDTO saloonDTO=saloonFeignClient.getSalonByOwnerId(jwt).getBody();
        Category saveCategory=service.createCategory(category,saloonDTO);
        return ResponseEntity.ok(saveCategory);
    }

    @GetMapping("/salon/{salonId}/category/{id}")
    public ResponseEntity<Category> getCategoryByIdAndSalon(@PathVariable Long id,@PathVariable Long salonId){

        Category category=service.findByIdAndSalonId(id,salonId);
        return ResponseEntity.ok(category);

    }

@DeleteMapping("/{id}")
public ResponseEntity<String> deleteCategory(@PathVariable Long id,@RequestHeader("Authorization")String jwt){

    SaloonDTO saloonDTO=saloonFeignClient.getSalonByOwnerId(jwt).getBody();

    service.deleteCategoryById(id,saloonDTO.getId());
        return ResponseEntity.ok("Category deleted successfully");
    }


}
