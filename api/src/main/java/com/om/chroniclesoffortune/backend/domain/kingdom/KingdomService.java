package com.om.chroniclesoffortune.backend.domain.kingdom;

import com.om.chroniclesoffortune.backend.domain.behaviorlog.UserAction;
import com.om.chroniclesoffortune.backend.domain.behaviorlog.UserActionEvent;
import com.om.chroniclesoffortune.backend.domain.kingdom.dto.*;
import com.om.chroniclesoffortune.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@NullMarked
@Service
@RequiredArgsConstructor
public class KingdomService {

    private final KingdomRepository kingdomRepository;
    private final KingdomStateRepository kingdomStateRepository;
    private final PlayerProgressRepository playerProgressRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public KingdomSummaryResponse createKingdom(User user, CreateKingdomRequest request) {
        if (kingdomRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalStateException("KINGDOM_ALREADY_EXISTS");
        }

        Kingdom kingdom = kingdomRepository.saveAndFlush(
                Kingdom.builder()
                        .user(user)
                        .name(request.name())
                        .build()
        );

        KingdomState state = kingdomStateRepository.save(
                KingdomState.builder()
                        .kingdom(kingdom)
                        .gold(BigDecimal.ZERO)
                        .mana(BigDecimal.ZERO)
                        .resilience(BigDecimal.ZERO)
                        .stability(new BigDecimal("100.00"))
                        .build()
        );

        playerProgressRepository.save(
                PlayerProgress.builder()
                        .user(user)
                        .kingdom(kingdom)
                        .level(1)
                        .experiencePoints(0)
                        .build()
        );

        eventPublisher.publishEvent(new UserActionEvent(user, UserAction.KINGDOM_CREATED, null));

        return toSummary(kingdom, state);
    }

    @Transactional
    public KingdomSummaryResponse updateKingdom(User user, UpdateKingdomRequest request) {
        Kingdom kingdom = kingdomRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        if (request.name() != null) kingdom.setName(request.name());

        kingdomRepository.save(kingdom);

        KingdomState state = kingdomStateRepository.findByKingdomId(kingdom.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        return toSummary(kingdom, state);
    }

    public KingdomSummaryResponse getMyKingdom(User user) {
        Kingdom kingdom = kingdomRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        KingdomState state = kingdomStateRepository.findByKingdomId(kingdom.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        return toSummary(kingdom, state);
    }

    private KingdomSummaryResponse toSummary(Kingdom kingdom, KingdomState state) {
        return new KingdomSummaryResponse(
                new KingdomResponse(kingdom.getId(), kingdom.getName(), kingdom.getCreatedAt()),
                new KingdomStateResponse(state.getGold(), state.getMana(), state.getResilience(), state.getStability())
        );
    }
}
