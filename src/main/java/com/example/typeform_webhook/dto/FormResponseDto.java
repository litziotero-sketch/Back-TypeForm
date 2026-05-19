package com.example.typeform_webhook.dto;

import java.util.List;
import java.util.Map;

public class FormResponseDto {

    private String responseId;
    private String submittedAt;
    private Map<String, String> hiddenFields;
    private List<AnswerDto> answers;

    public FormResponseDto(
            String responseId,
            String submittedAt,
            Map<String, String> hiddenFields,
            List<AnswerDto> answers
    ) {
        this.responseId = responseId;
        this.submittedAt = submittedAt;
        this.hiddenFields = hiddenFields;
        this.answers = answers;
    }

    public String getResponseId() {
        return responseId;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public Map<String, String> getHiddenFields() {
        return hiddenFields;
    }

    public List<AnswerDto> getAnswers() {
        return answers;
    }
}