package com.identity.platform.error;

public class CLIENT_CREDENTIALS_LOAD_ERROR extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CLIENT_CREDENTIALS_LOAD_ERROR(String message) {
        super(message);

    }
}
