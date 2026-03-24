package com.om.chroniclesoffortune.backend.domain.narrative.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

import java.util.List;

public record CreateChoiceRequest(
        @NotBlank @Size(max = 500) String text,
        @Nullable String description,
        @NotNull Integer displayOrder,
        @NotNull @Valid List<CreateEffectRequest> effects
) {}
