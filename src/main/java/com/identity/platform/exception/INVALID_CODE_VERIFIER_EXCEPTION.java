package com.identity.platform.exception;

public class INVALID_CODE_VERIFIER_EXCEPTION extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public INVALID_CODE_VERIFIER_EXCEPTION(String message) {
        super(message);

    }
}
