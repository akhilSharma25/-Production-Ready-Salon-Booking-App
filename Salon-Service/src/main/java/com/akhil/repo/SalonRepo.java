package com.akhil.repo;

import com.akhil.model.Saloon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SalonRepo extends JpaRepository<Saloon,Long> {

    Optional<Saloon> findByOwnerId(Long id);

    @Query("SELECT s FROM Saloon s WHERE LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Saloon> searchSalon(@Param("keyword") String keyword);
}
