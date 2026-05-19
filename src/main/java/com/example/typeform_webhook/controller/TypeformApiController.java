package com.example.typeform_webhook.controller;

import com.example.typeform_webhook.dto.CreateFormRequest;
import com.example.typeform_webhook.service.TypeformApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/typeform")
public class TypeformApiController {

    private final TypeformApiService service;

    public TypeformApiController(TypeformApiService service) {
        this.service = service;
    }

    @PostMapping("/forms")
    public ResponseEntity<String> createForm(
            @RequestBody CreateFormRequest request
    ) {
        String response = service.createForm(request);
        return ResponseEntity.ok(response);
    }
}