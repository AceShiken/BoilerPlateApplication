package com.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "name is required")
    private String name;

    @Email(message = "email must be valid")
    private String email;

    private String phone;
}