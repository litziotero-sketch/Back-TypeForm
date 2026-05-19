package com.example.typeform_webhook.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class TypeformFormListResponse {
    private String formId;
    private String title;
    private String description;
    private String workspaceName;
    private Integer questionCount;
    private Integer submissionCount;
    private Boolean isPublished;
    private Instant createdAt;
    private Instant lastUpdatedAt;
}