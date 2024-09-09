package com.identity.platform.error;

public class SIGNING_KEY_LOAD_ERROR extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SIGNING_KEY_LOAD_ERROR(String message) {
        super(message);

    }
}
