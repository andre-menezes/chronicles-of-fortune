package com.om.chroniclesoffortune.backend.domain.behaviorlog;

import com.om.chroniclesoffortune.backend.domain.behaviorlog.dto.BehaviorLogResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@NullMarked
@Service
@RequiredArgsConstructor
public class BehaviorLogService {

    private final BehaviorLogRepository behaviorLogRepository;

    public List<BehaviorLogResponse> findAll(@Nullable UUID userId, @Nullable String action) {
        if (userId != null) {
            return behaviorLogRepository.findByUserIdOrderByCreatedAtDesc(userId)
                    .stream().map(this::toResponse).toList();
        }
        if (action != null) {
            return behaviorLogRepository.findByActionOrderByCreatedAtDesc(UserAction.valueOf(action))
                    .stream().map(this::toResponse).toList();
        }
        return behaviorLogRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    // ─── private helpers ──────────────────────────────────────────────────────

    private BehaviorLogResponse toResponse(BehaviorLog log) {
        return new BehaviorLogResponse(
                log.getId(),
                log.getUser() != null ? log.getUser().getId() : null,
                log.getAction().name(),
                log.getMetadata(),
                log.getCreatedAt()
        );
    }
}
