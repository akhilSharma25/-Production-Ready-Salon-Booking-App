package com.akhil.controller;

import com.akhil.model.Category;
import com.akhil.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/salon/{id}")
    public ResponseEntity<Set<Category>> getCategoriesBySalon(@PathVariable("id") Long salonId){

        Set<Category> categories=service.getAllCategoriesBySalon(salonId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id){

        Category categories=service.getCategoryById(id);
        return ResponseEntity.ok(categories);
    }



}
