package com.example.typeform_webhook.dto;

public class AnswerDto {

    private String fieldId;
    private String question;
    private String type;
    private Object value;

    public AnswerDto(String fieldId, String question, String type, Object value) {
        this.fieldId = fieldId;
        this.question = question;
        this.type = type;
        this.value = value;
    }

    public String getFieldId() {
        return fieldId;
    }

    public String getQuestion() {
        return question;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}