package com.joolove.core.utils.handler;

import com.joolove.core.model.RestAPIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class RestControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestAPIResponse<String>> handleException(Exception e) {
        return ResponseEntity
                .badRequest()
                .body(RestAPIResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

}