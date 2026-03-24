package com.om.chroniclesoffortune.backend.domain.tip;

import com.om.chroniclesoffortune.backend.domain.tip.dto.ContextualTipResponse;
import com.om.chroniclesoffortune.backend.domain.tip.dto.CreateContextualTipRequest;
import com.om.chroniclesoffortune.backend.domain.tip.dto.UpdateContextualTipRequest;
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
public class ContextualTipService {

    private final ContextualTipRepository contextualTipRepository;

    @Transactional
    public ContextualTipResponse create(CreateContextualTipRequest request) {
        ContextualTip saved = contextualTipRepository.saveAndFlush(
                ContextualTip.builder()
                        .text(request.text())
                        .context(TipContext.valueOf(request.context()))
                        .category(TipCategory.valueOf(request.category()))
                        .active(true)
                        .build()
        );
        return toResponse(saved);
    }

    public List<ContextualTipResponse> findAll() {
        return contextualTipRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ContextualTipResponse> findByContext(String context) {
        return contextualTipRepository.findByContextAndActiveTrue(TipContext.valueOf(context))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ContextualTipResponse update(UUID id, UpdateContextualTipRequest request) {
        ContextualTip tip = getOrThrow(id);

        if (request.text() != null) tip.setText(request.text());
        if (request.context() != null) tip.setContext(TipContext.valueOf(request.context()));
        if (request.category() != null) tip.setCategory(TipCategory.valueOf(request.category()));
        if (request.active() != null) tip.setActive(request.active());

        return toResponse(contextualTipRepository.save(tip));
    }

    @Transactional
    public void delete(UUID id) {
        getOrThrow(id);
        contextualTipRepository.deleteById(id);
    }

    // ─── private helpers ──────────────────────────────────────────────────────

    private ContextualTip getOrThrow(UUID id) {
        return contextualTipRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("CONTEXTUAL_TIP_NOT_FOUND"));
    }

    private ContextualTipResponse toResponse(ContextualTip tip) {
        return new ContextualTipResponse(
                tip.getId(),
                tip.getText(),
                tip.getContext().name(),
                tip.getCategory().name(),
                tip.isActive(),
                tip.getCreatedAt()
        );
    }
}
