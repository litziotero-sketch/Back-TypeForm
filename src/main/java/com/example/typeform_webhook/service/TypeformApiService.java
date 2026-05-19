package com.example.typeform_webhook.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.typeform_webhook.dto.CreateFormRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TypeformApiService {
     private final WebClient webClient;

    public TypeformApiService(
            @Value("${typeform.api.token}") String token
    ) {

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

    public String createForm(CreateFormRequest request) {

        return webClient.post()
                .uri("/forms")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
