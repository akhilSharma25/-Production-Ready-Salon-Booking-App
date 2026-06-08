package com.akhil.service;

import com.akhil.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);
    User getUserById(Long id);
    String deleteUser(Long id);
    User updateUser(Long id,User user);

    List<User> getAllUser();
}
