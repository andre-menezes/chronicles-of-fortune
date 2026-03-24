package com.om.chroniclesoffortune.backend.domain.tip.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateContextualTipRequest(
        @NotBlank @Size(min = 10, max = 500) String text,
        @NotBlank String context,
        @NotBlank String category
) {}
