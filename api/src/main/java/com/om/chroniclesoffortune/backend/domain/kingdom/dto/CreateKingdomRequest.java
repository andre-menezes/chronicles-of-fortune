package com.om.chroniclesoffortune.backend.domain.kingdom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateKingdomRequest(
        @NotBlank(message = "Kingdom name is required")
        @Size(min = 3, max = 100, message = "Kingdom name must be between 3 and 100 characters")
        String name
) {}
