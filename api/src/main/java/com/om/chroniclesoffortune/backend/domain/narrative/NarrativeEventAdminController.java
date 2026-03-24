package com.om.chroniclesoffortune.backend.domain.narrative;

import com.om.chroniclesoffortune.backend.domain.narrative.dto.CreateNarrativeEventRequest;
import com.om.chroniclesoffortune.backend.domain.narrative.dto.NarrativeEventDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@NullMarked
@RestController
@RequestMapping("/admin/narrative-events")
@RequiredArgsConstructor
public class NarrativeEventAdminController {

    private final NarrativeEventService narrativeEventService;

    @PostMapping
    public ResponseEntity<NarrativeEventDetailResponse> create(
            @Valid @RequestBody CreateNarrativeEventRequest request) {
        return ResponseEntity.ok(narrativeEventService.createEvent(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NarrativeEventDetailResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateNarrativeEventRequest request) {
        return ResponseEntity.ok(narrativeEventService.updateEvent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        narrativeEventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
