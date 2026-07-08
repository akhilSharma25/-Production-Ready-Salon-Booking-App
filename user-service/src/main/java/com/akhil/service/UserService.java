package com.akhil.service;

import com.akhil.model.User;
import com.akhil.payload.DTO.UserDTO;

import java.util.List;

public interface UserService {

    User createUser(User user);
    UserDTO getUserById(Long id);
    String deleteUser(Long id);
    User updateUser(Long id,User user);

    List<User> getAllUser();
    UserDTO getUserFromJwt(String jwt);
}
