package com.om.chroniclesoffortune.backend.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        @Pattern(
                regexp = "^[\\p{L}]+(\\s[\\p{L}]+)+$",
                message = "Name must include first and last name and contain only letters"
        )
        String name,

        @NotBlank(message = "Username is required")
        @Pattern(
                regexp = "^[a-zA-Z][a-zA-Z0-9_]{2,49}$",
                message = "Username must start with a letter and contain only letters, numbers, and underscores (3-50 characters)"
        )
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid address")
        @Size(max = 255, message = "Email must not exceed 255 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
                message = "Password must contain at least one letter, one number, and one special character"
        )
        String password,

        @NotBlank(message = "Password confirmation is required")
        String confirmPassword

) {}
