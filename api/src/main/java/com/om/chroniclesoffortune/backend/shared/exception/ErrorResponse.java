package com.om.chroniclesoffortune.backend.shared.exception;

import java.util.List;

public record ErrorResponse(
        int status,
        String error,
        String code,
        String message,
        String timestamp,
        String path,
        List<FieldError> errors
) {}
