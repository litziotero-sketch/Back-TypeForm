package com.example.typeform_webhook.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success = true;
    private String message;
    private Instant timestamp = Instant.now();
    private T data;
    private PaginationResponse pagination;
    private String errorCode;
}