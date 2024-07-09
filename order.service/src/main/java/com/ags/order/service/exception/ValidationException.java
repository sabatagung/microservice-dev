package com.ags.order.service.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{
    private final String field;

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

}