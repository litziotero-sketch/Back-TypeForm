package com.example.typeform_webhook.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TypeformClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public TypeformClient(
            @Value("${typeform.api.token}") String token
    ) {

        this.objectMapper = new ObjectMapper();

        this.webClient = WebClient.builder()
                .baseUrl("https://api.typeform.com")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    public JsonNode getForms(Integer page, Integer size, String search) {

        try {

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forms")
                            .queryParam("page", page != null ? page : 0)
                            .queryParam("page_size", size != null ? size : 20)
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
            throw new RuntimeException("Error fetching forms from Typeform", e);
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
            throw new RuntimeException("Error fetching form details", e);
        }
    }
}