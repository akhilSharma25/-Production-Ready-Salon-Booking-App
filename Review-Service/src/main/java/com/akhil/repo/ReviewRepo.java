package com.akhil.repo;

import com.akhil.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review,Long> {

    List<Review> findBySalonId(Long salonId);
}
