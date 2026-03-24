package com.om.chroniclesoffortune.backend.domain.narrative.dto;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record ChoiceResponse(
        UUID id,
        String text,
        @Nullable String description,
        int displayOrder
) {}
