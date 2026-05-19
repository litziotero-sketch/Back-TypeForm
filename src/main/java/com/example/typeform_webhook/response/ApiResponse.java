package com.example.typeform_webhook.response;

import java.time.Instant;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private Instant timestamp;
    private PaginationResponse pagination;
    private T data;

    public ApiResponse(boolean success, String message, PaginationResponse pagination, T data) {
        this.success = success;
        this.message = message;
        this.timestamp = Instant.now();
        this.pagination = pagination;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(String message, PaginationResponse pagination, T data) {
        return new ApiResponse<>(true, message, pagination, data);
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

    public PaginationResponse getPagination() {
        return pagination;
    }

    public T getData() {
        return data;
    }
}