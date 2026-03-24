package com.om.chroniclesoffortune.backend.domain.narrative;

import com.om.chroniclesoffortune.backend.domain.behaviorlog.UserAction;
import com.om.chroniclesoffortune.backend.domain.behaviorlog.UserActionEvent;
import com.om.chroniclesoffortune.backend.domain.kingdom.KingdomRepository;
import com.om.chroniclesoffortune.backend.domain.kingdom.KingdomState;
import com.om.chroniclesoffortune.backend.domain.kingdom.KingdomStateRepository;
import com.om.chroniclesoffortune.backend.domain.kingdom.PlayerProgress;
import com.om.chroniclesoffortune.backend.domain.kingdom.PlayerProgressRepository;
import com.om.chroniclesoffortune.backend.domain.kingdom.dto.KingdomStateResponse;
import com.om.chroniclesoffortune.backend.domain.narrative.dto.*;
import com.om.chroniclesoffortune.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@NullMarked
@Service
@RequiredArgsConstructor
public class NarrativeEventService {

    private final NarrativeEventRepository narrativeEventRepository;
    private final ResolvedEventRepository resolvedEventRepository;
    private final KingdomRepository kingdomRepository;
    private final KingdomStateRepository kingdomStateRepository;
    private final PlayerProgressRepository playerProgressRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Optional<NarrativeEventSummaryResponse> getNextEvent(User user) {
        var kingdom = kingdomRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        var state = kingdomStateRepository.findByKingdomId(kingdom.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        var progress = playerProgressRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        Set<UUID> resolvedIds = resolvedEventRepository.findByUserId(user.getId()).stream()
                .map(re -> re.getNarrativeEvent().getId())
                .collect(Collectors.toSet());

        return narrativeEventRepository
                .findByRequiredLevelLessThanEqualOrderByPriorityDesc(progress.getLevel())
                .stream()
                .filter(event -> !resolvedIds.contains(event.getId()))
                .filter(event -> allRulesPass(event.getTriggerRules(), state, progress))
                .findFirst()
                .map(this::toSummary);
    }

    public NarrativeEventDetailResponse getEvent(UUID id) {
        NarrativeEvent event = narrativeEventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("EVENT_NOT_FOUND"));

        List<ChoiceResponse> choices = event.getChoices().stream()
                .sorted((a, b) -> Integer.compare(a.getDisplayOrder(), b.getDisplayOrder()))
                .map(c -> new ChoiceResponse(c.getId(), c.getText(), c.getDescription(), c.getDisplayOrder()))
                .toList();

        return new NarrativeEventDetailResponse(event.getId(), event.getTitle(), event.getDescription(), choices);
    }

    @Transactional
    public ResolveChoiceResponse resolveChoice(User user, UUID eventId, UUID choiceId) {
        NarrativeEvent event = narrativeEventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("EVENT_NOT_FOUND"));

        Choice choice = event.getChoices().stream()
                .filter(c -> c.getId().equals(choiceId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("CHOICE_NOT_FOUND"));

        if (resolvedEventRepository.existsByUserIdAndNarrativeEventId(user.getId(), eventId)) {
            throw new IllegalStateException("EVENT_ALREADY_RESOLVED");
        }

        var kingdom = kingdomRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        KingdomState state = kingdomStateRepository.findByKingdomId(kingdom.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        PlayerProgress progress = playerProgressRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        for (Effect effect : choice.getEffects()) {
            applyEffect(effect, state, progress);
        }

        kingdomStateRepository.saveAndFlush(state);
        playerProgressRepository.save(progress);

        resolvedEventRepository.save(
                ResolvedEvent.builder()
                        .user(user)
                        .narrativeEvent(event)
                        .choice(choice)
                        .resolvedAt(LocalDateTime.now())
                        .build()
        );

        eventPublisher.publishEvent(new UserActionEvent(user, UserAction.NARRATIVE_CHOICE_RESOLVED,
                "{\"eventId\":\"" + eventId + "\",\"choiceId\":\"" + choiceId + "\"}"));

        return new ResolveChoiceResponse(
                new KingdomStateResponse(state.getGold(), state.getMana(), state.getResilience(), state.getStability()),
                new PlayerProgressResponse(progress.getLevel(), progress.getExperiencePoints())
        );
    }

    @Transactional
    public NarrativeEventDetailResponse createEvent(CreateNarrativeEventRequest request) {
        NarrativeEvent event = NarrativeEvent.builder()
                .title(request.title())
                .description(request.description())
                .narrativePhase(request.narrativePhase())
                .requiredLevel(request.requiredLevel())
                .priority(request.priority())
                .build();

        for (CreateChoiceRequest choiceReq : request.choices()) {
            event.getChoices().add(buildChoice(event, choiceReq));
        }

        for (CreateTriggerRuleRequest ruleReq : request.triggerRules()) {
            event.getTriggerRules().add(buildTriggerRule(event, ruleReq));
        }

        NarrativeEvent saved = narrativeEventRepository.saveAndFlush(event);
        return getEvent(saved.getId());
    }

    @Transactional
    public NarrativeEventDetailResponse updateEvent(UUID id, CreateNarrativeEventRequest request) {
        NarrativeEvent event = narrativeEventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("EVENT_NOT_FOUND"));

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setNarrativePhase(request.narrativePhase());
        event.setRequiredLevel(request.requiredLevel());
        event.setPriority(request.priority());

        narrativeEventRepository.save(event);
        return getEvent(event.getId());
    }

    @Transactional
    public void deleteEvent(UUID id) {
        if (!narrativeEventRepository.existsById(id)) {
            throw new NoSuchElementException("EVENT_NOT_FOUND");
        }
        narrativeEventRepository.deleteById(id);
    }

    // ─── private helpers ──────────────────────────────────────────────────────

    private Choice buildChoice(NarrativeEvent event, CreateChoiceRequest req) {
        Choice choice = Choice.builder()
                .narrativeEvent(event)
                .text(req.text())
                .description(req.description())
                .displayOrder(req.displayOrder())
                .build();

        for (CreateEffectRequest effectReq : req.effects()) {
            choice.getEffects().add(Effect.builder()
                    .choice(choice)
                    .effectType(EffectType.valueOf(effectReq.effectType()))
                    .value(effectReq.value())
                    .build());
        }

        return choice;
    }

    private TriggerRule buildTriggerRule(NarrativeEvent event, CreateTriggerRuleRequest req) {
        return TriggerRule.builder()
                .narrativeEvent(event)
                .conditionType(ConditionType.valueOf(req.conditionType()))
                .operator(req.operator() != null ? RuleOperator.valueOf(req.operator()) : null)
                .thresholdValue(req.thresholdValue())
                .probability(req.probability())
                .build();
    }

    private void applyEffect(Effect effect, KingdomState state, PlayerProgress progress) {
        BigDecimal value = effect.getValue();
        switch (effect.getEffectType()) {
            case GOLD_CHANGE -> state.setGold(state.getGold().add(value));
            case MANA_CHANGE -> state.setMana(state.getMana().add(value));
            case RESILIENCE_CHANGE -> state.setResilience(state.getResilience().add(value));
            case STABILITY_CHANGE -> {
                BigDecimal newStability = state.getStability().add(value);
                state.setStability(newStability.max(BigDecimal.ZERO).min(new BigDecimal("100")));
            }
            case XP_GRANT -> progress.setExperiencePoints(
                    progress.getExperiencePoints() + value.intValue()
            );
        }
    }

    private boolean allRulesPass(List<TriggerRule> rules, KingdomState state, PlayerProgress progress) {
        for (TriggerRule rule : rules) {
            if (!rulePass(rule, state, progress)) {
                return false;
            }
        }
        return true;
    }

    private boolean rulePass(TriggerRule rule, KingdomState state, PlayerProgress progress) {
        if (rule.getConditionType() == ConditionType.RANDOM) {
            BigDecimal probability = rule.getProbability();
            return probability != null && Math.random() <= probability.doubleValue();
        }

        BigDecimal fieldValue = resolveField(rule.getConditionType(), state, progress);
        BigDecimal threshold = rule.getThresholdValue();
        RuleOperator operator = rule.getOperator();

        if (fieldValue == null || threshold == null || operator == null) {
            return false;
        }

        int cmp = fieldValue.compareTo(threshold);
        return switch (operator) {
            case GT -> cmp > 0;
            case LT -> cmp < 0;
            case GTE -> cmp >= 0;
            case LTE -> cmp <= 0;
            case EQ -> cmp == 0;
        };
    }

    private BigDecimal resolveField(ConditionType type, KingdomState state, PlayerProgress progress) {
        return switch (type) {
            case GOLD_THRESHOLD -> state.getGold();
            case MANA_THRESHOLD -> state.getMana();
            case RESILIENCE_THRESHOLD -> state.getResilience();
            case STABILITY_THRESHOLD -> state.getStability();
            case LEVEL_THRESHOLD -> BigDecimal.valueOf(progress.getLevel());
            case RANDOM -> null;
        };
    }

    private NarrativeEventSummaryResponse toSummary(NarrativeEvent event) {
        return new NarrativeEventSummaryResponse(event.getId(), event.getTitle(), event.getDescription());
    }
}
