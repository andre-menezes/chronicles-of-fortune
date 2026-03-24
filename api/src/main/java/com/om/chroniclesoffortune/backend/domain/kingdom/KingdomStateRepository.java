package com.om.chroniclesoffortune.backend.domain.kingdom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KingdomStateRepository extends JpaRepository<KingdomState, UUID> {
    Optional<KingdomState> findByKingdomId(UUID kingdomId);
}
