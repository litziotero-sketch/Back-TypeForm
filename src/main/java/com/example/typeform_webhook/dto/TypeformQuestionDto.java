package com.example.typeform_webhook.dto;

import lombok.Data;

@Data
public class TypeformQuestionDto {
    private String fieldId;
    private String title;
    private String type;
    private Boolean required;
}