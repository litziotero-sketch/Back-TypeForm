package com.example.typeform_webhook.controller;

import com.example.typeform_webhook.response.ApiResponse;
import com.example.typeform_webhook.dto.TypeformFormDetailResponse;
import com.example.typeform_webhook.dto.TypeformFormListResponse;
import com.example.typeform_webhook.service.TypeformService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/typeform")
public class TypeformController {

    private final TypeformService service;

    public TypeformController(TypeformService service) {
        this.service = service;
    }

    @GetMapping("/forms")
    public ResponseEntity<ApiResponse<List<TypeformFormListResponse>>> getForms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort) {

        return ResponseEntity.ok(service.getForms(page, size, search, sort));
    }

    @GetMapping("/forms/{formId}")
    public ResponseEntity<ApiResponse<TypeformFormDetailResponse>> getFormById(
            @PathVariable String formId) {

        return ResponseEntity.ok(service.getFormById(formId));
    }
}