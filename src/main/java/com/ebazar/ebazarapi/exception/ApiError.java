package com.ebazar.ebazarapi.exception;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiError {

    private OffsetDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;

    // Only present for validation errors
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationError> validationErrors;
}