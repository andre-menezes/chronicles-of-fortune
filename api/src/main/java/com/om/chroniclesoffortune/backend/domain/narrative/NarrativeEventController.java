package com.om.chroniclesoffortune.backend.domain.narrative;

import com.om.chroniclesoffortune.backend.domain.narrative.dto.NarrativeEventDetailResponse;
import com.om.chroniclesoffortune.backend.domain.narrative.dto.NarrativeEventSummaryResponse;
import com.om.chroniclesoffortune.backend.domain.narrative.dto.ResolveChoiceResponse;
import com.om.chroniclesoffortune.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@NullMarked
@RestController
@RequestMapping("/narrative-events")
@RequiredArgsConstructor
public class NarrativeEventController {

    private final NarrativeEventService narrativeEventService;

    @GetMapping("/next")
    public ResponseEntity<NarrativeEventSummaryResponse> getNextEvent(@AuthenticationPrincipal User user) {
        return narrativeEventService.getNextEvent(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NarrativeEventDetailResponse> getEvent(@PathVariable UUID id) {
        return ResponseEntity.ok(narrativeEventService.getEvent(id));
    }

    @PostMapping("/{id}/choices/{choiceId}/resolve")
    public ResponseEntity<ResolveChoiceResponse> resolveChoice(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @PathVariable UUID choiceId) {
        return ResponseEntity.ok(narrativeEventService.resolveChoice(user, id, choiceId));
    }
}
