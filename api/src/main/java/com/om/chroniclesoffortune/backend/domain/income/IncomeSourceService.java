package com.om.chroniclesoffortune.backend.domain.income;

import com.om.chroniclesoffortune.backend.domain.income.dto.CreateIncomeSourceRequest;
import com.om.chroniclesoffortune.backend.domain.income.dto.IncomeSourceResponse;
import com.om.chroniclesoffortune.backend.domain.income.dto.UpdateIncomeSourceRequest;
import com.om.chroniclesoffortune.backend.domain.kingdom.KingdomRepository;
import com.om.chroniclesoffortune.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@NullMarked
@Service
@RequiredArgsConstructor
public class IncomeSourceService {

    private final IncomeSourceRepository incomeSourceRepository;
    private final KingdomRepository kingdomRepository;

    @Transactional
    public IncomeSourceResponse create(User user, CreateIncomeSourceRequest request) {
        var kingdom = kingdomRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("KINGDOM_NOT_FOUND"));

        IncomeSource saved = incomeSourceRepository.saveAndFlush(
                IncomeSource.builder()
                        .user(user)
                        .kingdom(kingdom)
                        .name(request.name())
                        .type(IncomeType.valueOf(request.type()))
                        .amount(request.amount())
                        .active(true)
                        .build()
        );

        return toResponse(saved);
    }

    public List<IncomeSourceResponse> findAll(User user) {
        return incomeSourceRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public IncomeSourceResponse findById(User user, UUID id) {
        return toResponse(getOwnedOrThrow(user, id));
    }

    @Transactional
    public IncomeSourceResponse update(User user, UUID id, UpdateIncomeSourceRequest request) {
        IncomeSource source = getOwnedOrThrow(user, id);

        if (request.name() != null) source.setName(request.name());
        if (request.type() != null) source.setType(IncomeType.valueOf(request.type()));
        if (request.amount() != null) source.setAmount(request.amount());
        if (request.active() != null) source.setActive(request.active());

        return toResponse(incomeSourceRepository.save(source));
    }

    @Transactional
    public void delete(User user, UUID id) {
        getOwnedOrThrow(user, id);
        incomeSourceRepository.deleteById(id);
    }

    // ─── private helpers ──────────────────────────────────────────────────────

    private IncomeSource getOwnedOrThrow(User user, UUID id) {
        return incomeSourceRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoSuchElementException("INCOME_SOURCE_NOT_FOUND"));
    }

    private IncomeSourceResponse toResponse(IncomeSource source) {
        return new IncomeSourceResponse(
                source.getId(),
                source.getName(),
                source.getType().name(),
                source.getAmount(),
                source.isActive(),
                source.getCreatedAt()
        );
    }
}
