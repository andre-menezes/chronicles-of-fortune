package com.om.chroniclesoffortune.backend.domain.income;

import com.om.chroniclesoffortune.backend.domain.income.dto.CreateIncomeSourceRequest;
import com.om.chroniclesoffortune.backend.domain.income.dto.IncomeSourceResponse;
import com.om.chroniclesoffortune.backend.domain.income.dto.UpdateIncomeSourceRequest;
import com.om.chroniclesoffortune.backend.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@NullMarked
@RestController
@RequestMapping("/income-sources")
@RequiredArgsConstructor
public class IncomeSourceController {

    private final IncomeSourceService incomeSourceService;

    @PostMapping
    public ResponseEntity<IncomeSourceResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateIncomeSourceRequest request) {
        return ResponseEntity.ok(incomeSourceService.create(user, request));
    }

    @GetMapping
    public ResponseEntity<List<IncomeSourceResponse>> findAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(incomeSourceService.findAll(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeSourceResponse> findById(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id) {
        return ResponseEntity.ok(incomeSourceService.findById(user, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeSourceResponse> update(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateIncomeSourceRequest request) {
        return ResponseEntity.ok(incomeSourceService.update(user, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id) {
        incomeSourceService.delete(user, id);
        return ResponseEntity.noContent().build();
    }
}
