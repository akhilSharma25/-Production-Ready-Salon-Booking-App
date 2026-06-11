package com.akhil.service.imp;

import com.akhil.DTO.SaloonDTO;
import com.akhil.model.Category;
import com.akhil.repo.CategoryRepo;
import com.akhil.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepo repo;
    @Override
    public Category createCategory(Category category, SaloonDTO saloonDTO) {

        category.setSalonId(saloonDTO.getId());
        return repo.save(category);
    }

    @Override
    public Set<Category> getAllCategoriesBySalon(Long id) {
        return repo.findBySalonId(id);
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category=repo.findById(id).orElse(null);
        if(category==null){
            throw  new RuntimeException("Category not exist");
        }
        return category;
    }



    public void deleteCategoryById(Long id,Long salonId) {
        Category category=getCategoryById(id);
        if(category.getSalonId()!=salonId){
            throw  new RuntimeException("You don't have permission to delete this category");
        }
        repo.deleteById(id);

    }
}
