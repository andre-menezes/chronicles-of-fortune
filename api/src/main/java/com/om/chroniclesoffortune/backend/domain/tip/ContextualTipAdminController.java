package com.om.chroniclesoffortune.backend.domain.tip;

import com.om.chroniclesoffortune.backend.domain.tip.dto.ContextualTipResponse;
import com.om.chroniclesoffortune.backend.domain.tip.dto.CreateContextualTipRequest;
import com.om.chroniclesoffortune.backend.domain.tip.dto.UpdateContextualTipRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@NullMarked
@RestController
@RequestMapping("/admin/contextual-tips")
@RequiredArgsConstructor
public class ContextualTipAdminController {

    private final ContextualTipService contextualTipService;

    @PostMapping
    public ResponseEntity<ContextualTipResponse> create(
            @Valid @RequestBody CreateContextualTipRequest request) {
        return ResponseEntity.ok(contextualTipService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ContextualTipResponse>> findAll() {
        return ResponseEntity.ok(contextualTipService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContextualTipResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateContextualTipRequest request) {
        return ResponseEntity.ok(contextualTipService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        contextualTipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
