package com.example.typeform_webhook.controller;

import com.example.typeform_webhook.dto.FormResponseDto;
import com.example.typeform_webhook.response.ApiResponse;
import com.example.typeform_webhook.service.TypeformService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/typeform")
public class TypeformController {

    private final TypeformService service;

    public TypeformController(TypeformService service) {
        this.service = service;
    }

    @GetMapping("/forms/{formId}/responses")
    public ApiResponse<List<FormResponseDto>> getFormResponses(
            @PathVariable String formId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String since
    ) {
        return service.getFormResponses(formId, page, size, completed, since);
    }
}