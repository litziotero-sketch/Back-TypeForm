package com.example.typeform_webhook.service;

import com.example.typeform_webhook.client.TypeformClient;
import com.example.typeform_webhook.dto.AnswerDto;
import com.example.typeform_webhook.dto.FormResponseDto;
import com.example.typeform_webhook.response.ApiResponse;
import com.example.typeform_webhook.response.PaginationResponse;
import tools.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TypeformService {

    private final TypeformClient client;

    public TypeformService(TypeformClient client) {
        this.client = client;
    }

    public ApiResponse<List<FormResponseDto>> getFormResponses(
            String formId,
            int page,
            int size,
            Boolean completed,
            String since
    ) {

        JsonNode raw = client.getFormResponses(
                formId,
                size,
                completed,
                since
        );

        List<FormResponseDto> responses = mapResponses(raw);

        int fromIndex = Math.min(page * size, responses.size());
        int toIndex = Math.min(fromIndex + size, responses.size());

        List<FormResponseDto> pageData =
                responses.subList(fromIndex, toIndex);

        long totalElements =
                raw.path("total_items").asLong(responses.size());

        int totalPages =
                size == 0
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
                "Responses retrieved successfully",
                pagination,
                pageData
        );
    }

    private List<FormResponseDto> mapResponses(JsonNode raw) {

        List<FormResponseDto> responses = new ArrayList<>();

        for (JsonNode item : raw.path("items")) {

            String responseId =
                    item.path("response_id")
                            .asString(
                                    item.path("token").asString()
                            );

            String submittedAt =
                    item.path("submitted_at").asString();

            Map<String, String> hiddenFields =
                    new HashMap<>();

            JsonNode hidden = item.path("hidden");

            hidden.properties().forEach(entry ->
                    hiddenFields.put(
                            entry.getKey(),
                            entry.getValue().asString()
                    )
            );

            List<AnswerDto> answers =
                    new ArrayList<>();

            for (JsonNode answer : item.path("answers")) {

                String fieldId =
                        answer.path("field")
                                .path("id")
                                .asString();

                String question =
                        answer.path("field")
                                .path("ref")
                                .asString("");

                String type =
                        answer.path("type")
                                .asString();

                Object value =
                        extractAnswerValue(answer, type);

                answers.add(
                        new AnswerDto(
                                fieldId,
                                question,
                                type,
                                value
                        )
                );
            }

            responses.add(
                    new FormResponseDto(
                            responseId,
                            submittedAt,
                            hiddenFields,
                            answers
                    )
            );
        }

        return responses;
    }

    private Object extractAnswerValue(
            JsonNode answer,
            String type
    ) {

        return switch (type) {

            case "text" ->
                    answer.path("text").asString();

            case "email" ->
                    answer.path("email").asString();

            case "number" ->
                    answer.path("number").doubleValue();

            case "boolean" ->
                    answer.path("boolean").booleanValue();

            case "date" ->
                    answer.path("date").asString();

            case "url" ->
                    answer.path("url").asString();

            case "choice" ->
                    answer.path("choice")
                            .path("label")
                            .asString();

            case "choices" -> {

                List<String> values =
                        new ArrayList<>();

                for (JsonNode label :
                        answer.path("choices")
                                .path("labels")) {

                    values.add(label.asString());
                }

                yield values;
            }

            default -> answer.toString();
        };
    }
}