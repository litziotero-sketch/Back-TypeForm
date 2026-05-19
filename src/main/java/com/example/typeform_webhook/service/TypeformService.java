package com.example.typeform_webhook.service;

import com.example.typeform_webhook.response.ApiResponse;
import com.example.typeform_webhook.response.PaginationResponse;
import com.example.typeform_webhook.client.TypeformClient;
import com.example.typeform_webhook.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TypeformService {

    private final TypeformClient client;

    public TypeformService(TypeformClient client) {
        this.client = client;
    }

    public ApiResponse<List<TypeformFormListResponse>> getForms(
            int page, int size, String search, String sort) {

        JsonNode root = client.getForms(page, size, search);

        List<TypeformFormListResponse> forms = StreamSupport.stream(
                        root.path("items").spliterator(), false)
                .map(this::toFormListDto)
                .collect(Collectors.toList());

        // Typeform devuelve total_items
        long totalItems = root.path("total_items").asLong(0);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        PaginationResponse pagination = new PaginationResponse(
                page, size, totalPages, totalItems, page < totalPages - 1
        );

        return new ApiResponse<>(
                true,
                "Forms retrieved successfully",
                null,
                forms,
                pagination,
                null
        );
    }

    public ApiResponse<TypeformFormDetailResponse> getFormById(String formId) {
        JsonNode form = client.getForm(formId);

        TypeformFormDetailResponse dto = toFormDetailDto(form);

        return new ApiResponse<>(
                true,
                "Form details retrieved successfully",
                null,
                dto,
                null,
                null
        );
    }

    private TypeformFormListResponse toFormListDto(JsonNode node) {
        TypeformFormListResponse dto = new TypeformFormListResponse();
        dto.setFormId(node.path("id").asText());
        dto.setTitle(node.path("title").asText());
        dto.setDescription(node.path("description").asText(null));
        dto.setWorkspaceName(node.path("workspace").path("name").asText("Default"));
        dto.setQuestionCount(node.path("fields").size());
        dto.setSubmissionCount(node.path("responses").asInt(0)); // a veces no viene
        dto.setIsPublished(true);
        dto.setCreatedAt(Instant.parse(node.path("created_at").asText()));
        dto.setLastUpdatedAt(Instant.parse(node.path("last_updated_at").asText()));
        return dto;
    }

    private TypeformFormDetailResponse toFormDetailDto(JsonNode node) {
        TypeformFormDetailResponse dto = new TypeformFormDetailResponse();
        dto.setFormId(node.path("id").asText());
        dto.setTitle(node.path("title").asText());
        dto.setDescription(node.path("description").asText(null));
        dto.setWorkspaceName(node.path("workspace").path("name").asText("Default"));
        dto.setIsPublished(true);
        // Agregar más campos según necesites...
        return dto;
    }
}