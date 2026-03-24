package com.om.chroniclesoffortune.backend.domain.income.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

public record UpdateIncomeSourceRequest(
        @Nullable @Size(min = 3, max = 100) String name,
        @Nullable String type,
        @Nullable @DecimalMin("0.01") BigDecimal amount,
        @Nullable Boolean active
) {}
