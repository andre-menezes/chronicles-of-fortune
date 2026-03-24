package com.om.chroniclesoffortune.backend.domain.behaviorlog;

import com.om.chroniclesoffortune.backend.domain.user.User;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record UserActionEvent(
        User user,
        UserAction action,
        @Nullable String metadata
) {}
