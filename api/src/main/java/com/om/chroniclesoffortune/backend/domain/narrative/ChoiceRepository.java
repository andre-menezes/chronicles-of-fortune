package com.om.chroniclesoffortune.backend.domain.narrative;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChoiceRepository extends JpaRepository<Choice, UUID> {
    List<Choice> findByNarrativeEventIdOrderByDisplayOrder(UUID narrativeEventId);
}
