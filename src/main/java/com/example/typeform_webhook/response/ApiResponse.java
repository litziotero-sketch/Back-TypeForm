package com.example.typeform_webhook.response;

import java.time.Instant;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private Instant timestamp;
    private PaginationResponse pagination;
    private T data;

    public ApiResponse() {
        this.timestamp = Instant.now();
    }

    public ApiResponse(
            boolean success,
            String message,
            PaginationResponse pagination,
            T data
    ) {
        this.success = success;
        this.message = message;
        this.pagination = pagination;
        this.data = data;
        this.timestamp = Instant.now(); // 👈 IMPORTANTE
    }

    public static <T> ApiResponse<T> ok(
            String message,
            PaginationResponse pagination,
            T data
    ) {
        return new ApiResponse<>(
                true,
                message,
                pagination,
                data
        );
    }

    public static <T> ApiResponse<T> error(
            String message
    ) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        response.setPagination(null);
        response.setTimestamp(Instant.now());
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public PaginationResponse getPagination() {
        return pagination;
    }

    public void setPagination(PaginationResponse pagination) {
        this.pagination = pagination;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}