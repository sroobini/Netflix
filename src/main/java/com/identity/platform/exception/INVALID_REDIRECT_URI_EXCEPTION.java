package com.identity.platform.exception;

public class INVALID_REDIRECT_URI_EXCEPTION extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public INVALID_REDIRECT_URI_EXCEPTION(String message) {
        super(message);

    }
}
