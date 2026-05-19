package com.example.typeform_webhook.client;


import tools.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TypeformClient {

    private final WebClient webClient;

    public TypeformClient(@Value("${typeform.api.token}") String token) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.typeform.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public JsonNode getFormResponses(String formId, int size, Boolean completed, String since) {
        return webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/forms/{formId}/responses")
                            .queryParam("page_size", size);

                    if (completed != null) {
                        builder.queryParam("completed", completed);
                    }

                    if (since != null && !since.isBlank()) {
                        builder.queryParam("since", since);
                    }

                    return builder.build(formId);
                })
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}