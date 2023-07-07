package com.joolove.core.security.jwt.utils;

import com.joolove.core.security.jwt.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice(annotations = RestController.class)
public class ControllerExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage handleTokenRefreshException(TokenRefreshException e, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false));
    }

//    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorMessage handleGlobalException(Exception e, WebRequest request) {
//        return new ErrorMessage(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                LocalDateTime.now(),
//                e.getMessage(),
//                request.getDescription(false));
//    }

}