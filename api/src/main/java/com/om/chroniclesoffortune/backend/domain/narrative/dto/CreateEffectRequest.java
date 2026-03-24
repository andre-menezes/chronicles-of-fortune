package com.om.chroniclesoffortune.backend.domain.narrative.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateEffectRequest(
        @NotBlank String effectType,
        @NotNull BigDecimal value
) {}
