package com.om.chroniclesoffortune.backend.domain.income.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateIncomeSourceRequest(
        @NotBlank @Size(min = 3, max = 100) String name,
        @NotBlank String type,
        @NotNull @DecimalMin("0.01") BigDecimal amount
) {}
