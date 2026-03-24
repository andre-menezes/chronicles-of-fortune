package com.om.chroniclesoffortune.backend.domain.behaviorlog.dto;

import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public record BehaviorLogResponse(
        UUID id,
        @Nullable UUID userId,
        String action,
        @Nullable String metadata,
        LocalDateTime createdAt
) {}
