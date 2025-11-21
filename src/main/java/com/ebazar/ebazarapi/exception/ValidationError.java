package com.ebazar.ebazarapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationError {

    // For body validations this is the field name, for others it may be a parameter/path
    private String field;

    private String message;
}