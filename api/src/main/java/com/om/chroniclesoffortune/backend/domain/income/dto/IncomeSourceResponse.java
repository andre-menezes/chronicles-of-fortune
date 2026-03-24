package com.om.chroniclesoffortune.backend.domain.income.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncomeSourceResponse(
        UUID id,
        String name,
        String type,
        BigDecimal amount,
        boolean active,
        LocalDateTime createdAt
) {}
