package com.joolove.core.security.jwt.exception;

import java.io.Serial;

public class ForbiddenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ForbiddenException(String msg) {
        super(msg);
    }
}
