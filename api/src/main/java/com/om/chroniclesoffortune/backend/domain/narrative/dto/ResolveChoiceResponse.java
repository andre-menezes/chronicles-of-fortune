package com.om.chroniclesoffortune.backend.domain.narrative.dto;

import com.om.chroniclesoffortune.backend.domain.kingdom.dto.KingdomStateResponse;

public record ResolveChoiceResponse(
        KingdomStateResponse state,
        PlayerProgressResponse progress
) {}
