package com.example.typeform_webhook.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
}