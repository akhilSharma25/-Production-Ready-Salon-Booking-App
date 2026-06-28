package com.akhil.model;


import com.akhil.domain.UserRole;
import com.akhil.repo.UserRepo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @NotBlank(message = "username is mandatory")
    private String username;
    @Email(message = "Email Should be valid")
    @NotBlank(message = "Email is mandatory")
    private  String email;
    private  String phone;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @Column(nullable = false)
    @NotNull(message = "role is mandatory")
    private UserRole role;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private  LocalDateTime updatedAt;
}
