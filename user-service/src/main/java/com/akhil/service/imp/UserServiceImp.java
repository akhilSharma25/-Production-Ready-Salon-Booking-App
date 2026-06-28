package com.akhil.service.imp;

import com.akhil.exception.UserException;
import com.akhil.model.User;
import com.akhil.payload.DTO.KeycloakUserDTO;
import com.akhil.repo.UserRepo;
import com.akhil.service.KeycloakService;
import com.akhil.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private KeycloakService keycloakService;

    @Override
    public User createUser(User user) {
        return repo.save(user);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> optional=repo.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        throw  new UserException("User not Found");    }

    @Override
    public String deleteUser(Long id) {
        User existingUser = repo.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        repo.deleteById(id);
        return "User Deleted with id : "+id;
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = repo.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        existingUser.setFullName(user.getFullName());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setRole(user.getRole());
        existingUser.setUpdatedAt(LocalDateTime.now()); // Manually sets update timestamp

        return repo.save(existingUser);
    }

    @Override
    public List<User> getAllUser() {
        return repo.findAll();
    }

    @Override
    public User getUserFromJwt(String jwt) {
        KeycloakUserDTO keycloakUserDTO=keycloakService.fetchUserProfileByJwt(jwt);
        User user=repo.findByEmail(keycloakUserDTO.getEmail());
        return user;
    }
}
