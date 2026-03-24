package com.om.chroniclesoffortune.backend.domain.behaviorlog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BehaviorLogRepository extends JpaRepository<BehaviorLog, UUID> {
    List<BehaviorLog> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<BehaviorLog> findByActionOrderByCreatedAtDesc(UserAction action);
    List<BehaviorLog> findAllByOrderByCreatedAtDesc();
}
