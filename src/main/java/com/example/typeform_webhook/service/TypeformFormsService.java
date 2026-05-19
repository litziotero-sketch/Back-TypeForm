package com.example.typeform_webhook.service;

import com.example.typeform_webhook.client.TypeformFormsClient;
import com.example.typeform_webhook.dto.TypeformFormDetailResponse;
import com.example.typeform_webhook.dto.TypeformFormListResponse;
import com.example.typeform_webhook.dto.TypeformQuestionDto;
import com.example.typeform_webhook.response.ApiResponse;
import com.example.typeform_webhook.response.PaginationResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class TypeformFormsService {

    private final TypeformFormsClient client;

    public TypeformFormsService(TypeformFormsClient client) {
        this.client = client;
    }

    // =========================
    // LIST FORMS
    // =========================
    public ApiResponse<List<TypeformFormListResponse>> getForms(
            int page,
            int size,
            String search
    ) {

        JsonNode raw = client.getForms(page, size, search);

        List<TypeformFormListResponse> forms = mapForms(raw);

        long totalElements = raw.path("total_items").asLong(forms.size());

        int totalPages = size == 0
                ? 0
                : (int) Math.ceil((double) totalElements / size);

        boolean hasNext = page + 1 < totalPages;

        PaginationResponse pagination = new PaginationResponse(
                page,
                size,
                totalPages,
                totalElements,
                hasNext
        );

        return ApiResponse.ok(
                "Forms retrieved successfully",
                pagination,
                forms
        );
    }

    // =========================
    // GET FORM BY ID
    // =========================
    public ApiResponse<TypeformFormDetailResponse> getFormById(String formId) {

        JsonNode raw = client.getForm(formId);

        TypeformFormDetailResponse form = mapFormDetail(raw);

        return ApiResponse.ok(
                "Form details retrieved successfully",
                null,
                form
        );
    }

    // =========================
    // MAPPERS - LIST
    // =========================
    private List<TypeformFormListResponse> mapForms(JsonNode raw) {

        List<TypeformFormListResponse> forms = new ArrayList<>();

        for (JsonNode item : raw.path("items")) {
            forms.add(mapSingleForm(item));
        }

        return forms;
    }

    private TypeformFormListResponse mapSingleForm(JsonNode item) {

        TypeformFormListResponse dto = new TypeformFormListResponse();

        dto.setFormId(item.path("id").asText());
        dto.setTitle(item.path("title").asText());
        dto.setDescription(item.path("description").asText(null));

        dto.setWorkspaceName(
                item.path("workspace")
                        .path("name")
                        .asText("Default")
        );

        dto.setQuestionCount(item.path("fields").size());
        dto.setSubmissionCount(item.path("responses_count").asInt(0));
        dto.setIsPublished(true);

        String createdAt = item.path("created_at").asText(null);
        if (createdAt != null) {
            dto.setCreatedAt(Instant.parse(createdAt));
        }

        String updatedAt = item.path("last_updated_at").asText(null);
        if (updatedAt != null) {
            dto.setLastUpdatedAt(Instant.parse(updatedAt));
        }

        return dto;
    }

    // =========================
    // MAPPER - DETAIL
    // =========================
    private TypeformFormDetailResponse mapFormDetail(JsonNode raw) {

        TypeformFormDetailResponse dto = new TypeformFormDetailResponse();

        dto.setFormId(raw.path("id").asText());
        dto.setTitle(raw.path("title").asText());
        dto.setDescription(raw.path("description").asText(null));

        dto.setWorkspaceName(
                raw.path("workspace")
                        .path("name")
                        .asText("Default")
        );

        dto.setIsPublished(true);

        String createdAt = raw.path("created_at").asText(null);
        if (createdAt != null) {
            dto.setCreatedAt(Instant.parse(createdAt));
        }

        String updatedAt = raw.path("last_updated_at").asText(null);
        if (updatedAt != null) {
            dto.setLastUpdatedAt(Instant.parse(updatedAt));
        }

        // FIX: themeId limpio (solo id, no URL)
        String themeHref = raw.path("theme")
                .path("href")
                .asText(null);

        if (themeHref != null) {
            String[] parts = themeHref.split("/");
            dto.setThemeId(parts[parts.length - 1]);
        }

        // QUESTIONS
        dto.setQuestions(mapQuestions(raw.path("fields")));

        return dto;
    }

    // =========================
    // QUESTIONS MAPPER
    // =========================
    private List<TypeformQuestionDto> mapQuestions(JsonNode fields) {

        List<TypeformQuestionDto> questions = new ArrayList<>();

        for (JsonNode field : fields) {

            TypeformQuestionDto q = new TypeformQuestionDto();

            q.setFieldId(field.path("id").asText());
            q.setTitle(field.path("title").asText());
            q.setType(field.path("type").asText());

            q.setRequired(
                    field.path("validations")
                            .path("required")
                            .asBoolean(false)
            );

            questions.add(q);
        }

        return questions;
    }
}