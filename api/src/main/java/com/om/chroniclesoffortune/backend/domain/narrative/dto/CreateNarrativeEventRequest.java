package com.om.chroniclesoffortune.backend.domain.narrative.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

import java.util.List;

public record CreateNarrativeEventRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank String description,
        @Nullable String narrativePhase,
        @NotNull Integer requiredLevel,
        @NotNull Integer priority,
        @NotNull @Valid List<CreateChoiceRequest> choices,
        @NotNull @Valid List<CreateTriggerRuleRequest> triggerRules
) {}
