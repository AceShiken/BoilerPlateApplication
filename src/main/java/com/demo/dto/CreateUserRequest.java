package com.demo.dto;

import com.demo.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "name is required")
    private String name;

    @Email(message = "email must be valid")
    private String email;

    @NotBlank
    private String phone;

    @NotNull(message = "role is required")
    private Role role;
}