package com.om.chroniclesoffortune.backend.domain.narrative.dto;

import java.util.UUID;

public record NarrativeEventSummaryResponse(
        UUID id,
        String title,
        String description
) {}
