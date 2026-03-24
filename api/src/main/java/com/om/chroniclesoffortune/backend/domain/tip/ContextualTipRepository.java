package com.om.chroniclesoffortune.backend.domain.tip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContextualTipRepository extends JpaRepository<ContextualTip, UUID> {
    List<ContextualTip> findByContextAndActiveTrue(TipContext context);
}
