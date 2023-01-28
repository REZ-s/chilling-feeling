package com.joolove.core.security.jwt.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Getter
public class ErrorMessage {

    private int statusCode;

    private LocalDateTime timestamp;

    private String message;

    private String description;

    @Builder
    public ErrorMessage(int statusCode, LocalDateTime timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
}
