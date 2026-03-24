package com.om.chroniclesoffortune.backend.shared.exception;

public record FieldError(String field, String message, Object rejectedValue) {}
