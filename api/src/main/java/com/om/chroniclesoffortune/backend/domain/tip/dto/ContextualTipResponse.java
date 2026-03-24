package com.om.chroniclesoffortune.backend.domain.tip.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContextualTipResponse(
        UUID id,
        String text,
        String context,
        String category,
        boolean active,
        LocalDateTime createdAt
) {}
