package com.akhil.controller;

import com.akhil.DTO.SaloonDTO;
import com.akhil.model.Category;
import com.akhil.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/categories/salon-owner")
public class SalonCategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping()
    public ResponseEntity<Category> createCategory(@RequestBody Category category){

        SaloonDTO saloonDTO=new SaloonDTO();
        saloonDTO.setId(1L);
        Category saveCategory=service.createCategory(category,saloonDTO);
        return ResponseEntity.ok(saveCategory);
    }


@DeleteMapping("/{id}")
public ResponseEntity<String> deleteCategory(@PathVariable Long id){

        SaloonDTO saloonDTO=new SaloonDTO();
        saloonDTO.setId(1L);
       service.deleteCategoryById(id,saloonDTO.getId());
        return ResponseEntity.ok("Category deleted successfully");
    }
}
