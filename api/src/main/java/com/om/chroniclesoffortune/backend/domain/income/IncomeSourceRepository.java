package com.om.chroniclesoffortune.backend.domain.income;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncomeSourceRepository extends JpaRepository<IncomeSource, UUID> {
    List<IncomeSource> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<IncomeSource> findByIdAndUserId(UUID id, UUID userId);
}
