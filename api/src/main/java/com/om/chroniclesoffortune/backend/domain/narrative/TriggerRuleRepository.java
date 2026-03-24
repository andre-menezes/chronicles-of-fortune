package com.om.chroniclesoffortune.backend.domain.narrative;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TriggerRuleRepository extends JpaRepository<TriggerRule, UUID> {
}
