package com.om.chroniclesoffortune.backend.domain.kingdom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KingdomRepository extends JpaRepository<Kingdom, UUID> {
    Optional<Kingdom> findByUserId(UUID userId);
}
