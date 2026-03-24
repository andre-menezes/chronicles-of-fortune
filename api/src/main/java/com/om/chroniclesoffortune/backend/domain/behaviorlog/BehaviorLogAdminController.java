package com.om.chroniclesoffortune.backend.domain.behaviorlog;

import com.om.chroniclesoffortune.backend.domain.behaviorlog.dto.BehaviorLogResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@NullMarked
@RestController
@RequestMapping("/admin/behavior-logs")
@RequiredArgsConstructor
public class BehaviorLogAdminController {

    private final BehaviorLogService behaviorLogService;

    @GetMapping
    public ResponseEntity<List<BehaviorLogResponse>> findAll(
            @Nullable @RequestParam(required = false) UUID userId,
            @Nullable @RequestParam(required = false) String action) {
        return ResponseEntity.ok(behaviorLogService.findAll(userId, action));
    }
}
