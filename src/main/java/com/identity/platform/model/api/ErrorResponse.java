package com.identity.platform.model.api;

public class ErrorResponse implements IResponse {

    private String errorMessage;

    public ErrorResponse(String errorMessage, String description) {
        this.errorMessage = errorMessage;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String description;
}