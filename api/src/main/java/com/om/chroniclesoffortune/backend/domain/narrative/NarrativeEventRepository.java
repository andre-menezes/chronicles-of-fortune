package com.om.chroniclesoffortune.backend.domain.narrative;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NarrativeEventRepository extends JpaRepository<NarrativeEvent, UUID> {
    List<NarrativeEvent> findByRequiredLevelLessThanEqualOrderByPriorityDesc(int level);
}
