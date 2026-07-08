package com.akhil.repo;

import com.akhil.model.User;
import com.akhil.payload.DTO.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User  findByEmail(String email);
}
