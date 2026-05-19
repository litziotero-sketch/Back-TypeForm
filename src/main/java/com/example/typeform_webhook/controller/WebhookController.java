package com.example.typeform_webhook.controller;

import com.example.typeform_webhook.service.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks/typeform")
public class WebhookController {

    private final WebhookService service;

    public WebhookController(WebhookService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> receiveWebhook(
            @RequestHeader(value = "Typeform-Signature", required = false) String signature,
            @RequestBody String rawBody
    ) {
        service.processWebhook(signature, rawBody);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Typeform webhook backend is running");
    }
}