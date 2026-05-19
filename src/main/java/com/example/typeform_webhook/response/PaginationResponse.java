package com.example.typeform_webhook.response;

public class PaginationResponse {

    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;

    public PaginationResponse(
            int page,
            int size,
            int totalPages,
            long totalElements,
            boolean hasNext
    ) {
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.hasNext = hasNext;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}