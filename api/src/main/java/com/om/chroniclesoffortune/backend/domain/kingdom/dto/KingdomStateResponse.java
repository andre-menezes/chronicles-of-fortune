package com.om.chroniclesoffortune.backend.domain.kingdom.dto;

import java.math.BigDecimal;

public record KingdomStateResponse(
        BigDecimal gold,
        BigDecimal mana,
        BigDecimal resilience,
        BigDecimal stability
) {}
