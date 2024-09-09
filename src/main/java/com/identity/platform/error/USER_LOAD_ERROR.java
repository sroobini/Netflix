package com.identity.platform.error;

public class USER_LOAD_ERROR extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public USER_LOAD_ERROR(String message) {
        super(message);

    }
}
