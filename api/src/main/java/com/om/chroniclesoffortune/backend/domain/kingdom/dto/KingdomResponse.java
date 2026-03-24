package com.om.chroniclesoffortune.backend.domain.kingdom.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record KingdomResponse(
        UUID id,
        String name,
        LocalDateTime createdAt
) {}
