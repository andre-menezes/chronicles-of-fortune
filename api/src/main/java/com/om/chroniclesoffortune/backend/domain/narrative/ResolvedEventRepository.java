package com.om.chroniclesoffortune.backend.domain.narrative;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResolvedEventRepository extends JpaRepository<ResolvedEvent, UUID> {
    boolean existsByUserIdAndNarrativeEventId(UUID userId, UUID narrativeEventId);
    List<ResolvedEvent> findByUserId(UUID userId);
}
