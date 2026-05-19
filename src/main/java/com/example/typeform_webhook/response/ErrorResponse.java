package com.example.typeform_webhook.response;

import java.time.Instant;

public class ErrorResponse {

    private boolean success;
    private String message;
    private Instant timestamp;
    private String errorCode;

    public ErrorResponse(String message, String errorCode) {
        this.success = false;
        this.message = message;
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }
}