package com.joolove.core.utils.exception;

import java.io.Serial;

public class AuthorizedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AuthorizedException(String msg) {
        super(msg);
    }
}
