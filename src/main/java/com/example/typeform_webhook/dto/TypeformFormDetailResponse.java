package com.example.typeform_webhook.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class TypeformFormDetailResponse {
    private String formId;
    private String title;
    private String description;
    private String workspaceName;
    private String themeId;
    private Boolean isPublished;
    private Instant createdAt;
    private Instant lastUpdatedAt;
    private List<TypeformQuestionDto> questions;
}