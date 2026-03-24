package com.om.chroniclesoffortune.backend.domain.kingdom;

import com.om.chroniclesoffortune.backend.domain.kingdom.dto.CreateKingdomRequest;
import com.om.chroniclesoffortune.backend.domain.kingdom.dto.KingdomSummaryResponse;
import com.om.chroniclesoffortune.backend.domain.kingdom.dto.UpdateKingdomRequest;
import com.om.chroniclesoffortune.backend.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@NullMarked
@RestController
@RequestMapping("/kingdoms")
@RequiredArgsConstructor
public class KingdomController {

    private final KingdomService kingdomService;

    @PostMapping
    public ResponseEntity<KingdomSummaryResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateKingdomRequest request) {
        return ResponseEntity.ok(kingdomService.createKingdom(user, request));
    }

    @GetMapping("/me")
    public ResponseEntity<KingdomSummaryResponse> getMyKingdom(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(kingdomService.getMyKingdom(user));
    }

    @PutMapping("/me")
    public ResponseEntity<KingdomSummaryResponse> update(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateKingdomRequest request) {
        return ResponseEntity.ok(kingdomService.updateKingdom(user, request));
    }
}
