package com.identity.platform.exception;

public class INVALID_CODE_CHALLENGE_EXCEPTION extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public INVALID_CODE_CHALLENGE_EXCEPTION(String message) {
        super(message);

    }
}
