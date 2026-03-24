package com.om.chroniclesoffortune.backend.domain.kingdom.dto;

public record KingdomSummaryResponse(
        KingdomResponse kingdom,
        KingdomStateResponse state
) {}
