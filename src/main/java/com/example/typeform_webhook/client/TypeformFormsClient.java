package com.example.typeform_webhook.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TypeformFormsClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public TypeformFormsClient(
            @Value("${typeform.api.token}") String token
    ) {

        this.objectMapper = new ObjectMapper();

        this.webClient = WebClient.builder()
                .baseUrl("https://api.typeform.com")
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + token
                )
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .build();
    }

    public JsonNode getForms(
            int page,
            int size,
            String search
    ) {

        try {

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forms")
                            .queryParam("page", page)
                            .queryParam("page_size", size)
                            .queryParamIfPresent(
                                    "search",
                                    java.util.Optional.ofNullable(search)
                            )
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return objectMapper.readTree(response);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error fetching forms",
                    e
            );
        }
    }

    public JsonNode getForm(String formId) {

        try {

            String response = webClient.get()
                    .uri("/forms/{formId}", formId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return objectMapper.readTree(response);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error fetching form",
                    e
            );
        }
    }
}