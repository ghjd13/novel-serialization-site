package com.novelplatform.novelsite.web.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        String error,
        String message,
        List<FieldError> fieldErrors) {

    public record FieldError(String field, String message) {}

    public static ErrorResponse of(String error, String message) {
        return new ErrorResponse(Instant.now(), error, message, List.of());
    }

    public static ErrorResponse of(String error, String message, List<FieldError> fieldErrors) {
        return new ErrorResponse(Instant.now(), error, message, List.copyOf(fieldErrors));
    }
}
