package com.om.chroniclesoffortune.backend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

public record UpdateUserRequest(
        @Nullable @Size(min = 2, max = 100) String name,
        @Nullable @Email String email,
        @Nullable String currentPassword,
        @Nullable @Size(min = 8) String newPassword
) {}
