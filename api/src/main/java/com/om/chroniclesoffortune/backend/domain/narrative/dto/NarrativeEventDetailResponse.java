package com.om.chroniclesoffortune.backend.domain.narrative.dto;

import java.util.List;
import java.util.UUID;

public record NarrativeEventDetailResponse(
        UUID id,
        String title,
        String description,
        List<ChoiceResponse> choices
) {}
