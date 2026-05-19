package com.example.typeform_webhook.controller;

import com.example.typeform_webhook.dto.TypeformFormDetailResponse;
import com.example.typeform_webhook.dto.TypeformFormListResponse;
import com.example.typeform_webhook.response.ApiResponse;
import com.example.typeform_webhook.service.TypeformFormsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/typeform")
public class TypeformFormsController {

    private final TypeformFormsService service;

    public TypeformFormsController(
            TypeformFormsService service
    ) {
        this.service = service;
    }

    @GetMapping("/forms")
    public ApiResponse<List<TypeformFormListResponse>> getForms(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            String search
    ) {

        return service.getForms(
                page,
                size,
                search
        );
    }

    @GetMapping("/forms/{formId}")
    public ApiResponse<TypeformFormDetailResponse> getFormById(
            @PathVariable String formId
    ) {

        return service.getFormById(formId);
    }
}