package com.akhil.service;

import com.akhil.DTO.SaloonDTO;
import com.akhil.model.Category;

import java.util.Set;

public interface CategoryService {

    Category createCategory(Category category, SaloonDTO saloonDTO);
    Set<Category> getAllCategoriesBySalon(Long id);
    Category getCategoryById(Long id);
    void deleteCategoryById(Long id,Long salonId);
}
