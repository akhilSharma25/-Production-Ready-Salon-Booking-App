package com.akhil.repo;

import com.akhil.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CategoryRepo extends JpaRepository<Category,Long> {

    Set<Category> findBySalonId(Long id);

    Category findByIdAndSalonId(Long id,Long salonId);
}
