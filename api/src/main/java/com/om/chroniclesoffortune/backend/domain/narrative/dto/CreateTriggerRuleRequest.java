package com.om.chroniclesoffortune.backend.domain.narrative.dto;

import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

public record CreateTriggerRuleRequest(
        @NotBlank String conditionType,
        @Nullable String operator,
        @Nullable BigDecimal thresholdValue,
        @Nullable BigDecimal probability
) {}
