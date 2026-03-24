package com.om.chroniclesoffortune.backend.domain.tip;

import com.om.chroniclesoffortune.backend.domain.behaviorlog.UserAction;
import com.om.chroniclesoffortune.backend.domain.behaviorlog.UserActionEvent;
import com.om.chroniclesoffortune.backend.domain.tip.dto.ContextualTipResponse;
import com.om.chroniclesoffortune.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@NullMarked
@RestController
@RequestMapping("/contextual-tips")
@RequiredArgsConstructor
public class ContextualTipController {

    private final ContextualTipService contextualTipService;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping
    public ResponseEntity<List<ContextualTipResponse>> findByContext(
            @AuthenticationPrincipal User user,
            @RequestParam String context) {
        eventPublisher.publishEvent(new UserActionEvent(user, UserAction.TIPS_VIEWED,
                "{\"context\":\"" + context + "\"}"));
        return ResponseEntity.ok(contextualTipService.findByContext(context));
    }
}
