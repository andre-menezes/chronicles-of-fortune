package com.om.chroniclesoffortune.backend.domain.kingdom.dto;

import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

public record UpdateKingdomRequest(
        @Nullable @Size(min = 3, max = 100) String name
) {}
