package com.om.chroniclesoffortune.backend.domain.behaviorlog;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@NullMarked
@Component
@RequiredArgsConstructor
public class BehaviorLogListener {

    private final BehaviorLogRepository behaviorLogRepository;

    @Async
    @EventListener
    public void handle(UserActionEvent event) {
        behaviorLogRepository.save(
                BehaviorLog.builder()
                        .user(event.user())
                        .action(event.action())
                        .metadata(event.metadata())
                        .build()
        );
    }
}
