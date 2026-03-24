package com.om.chroniclesoffortune.backend.domain.kingdom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlayerProgressRepository extends JpaRepository<PlayerProgress, UUID> {
    Optional<PlayerProgress> findByUserId(UUID userId);
}
