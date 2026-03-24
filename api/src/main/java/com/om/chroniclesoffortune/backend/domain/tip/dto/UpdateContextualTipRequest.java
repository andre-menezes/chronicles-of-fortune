package com.om.chroniclesoffortune.backend.domain.tip.dto;

import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

public record UpdateContextualTipRequest(
        @Nullable @Size(min = 10, max = 500) String text,
        @Nullable String context,
        @Nullable String category,
        @Nullable Boolean active
) {}
