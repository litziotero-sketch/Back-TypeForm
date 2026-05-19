package com.example.typeform_webhook.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class WebhookService {

    @Value("${typeform.webhook.secret}")
    private String webhookSecret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void processWebhook(String signatureHeader, String rawBody) {
        validateSignature(signatureHeader, rawBody);

        try {
            JsonNode payload = objectMapper.readTree(rawBody);

            String eventId = payload.path("event_id").asText();
            String eventType = payload.path("event_type").asText();
            JsonNode formResponse = payload.path("form_response");

            String formId = formResponse.path("form_id").asText();
            String submittedAt = formResponse.path("submitted_at").asText();
            JsonNode answers = formResponse.path("answers");

            System.out.println("Webhook received");
            System.out.println("Event ID: " + eventId);
            System.out.println("Event Type: " + eventType);
            System.out.println("Form ID: " + formId);
            System.out.println("Submitted At: " + submittedAt);
            System.out.println("Answers:");
            System.out.println(answers.toPrettyString());

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid Typeform payload"
            );
        }
    }

    private void validateSignature(String signatureHeader, String rawBody) {
        if (signatureHeader == null || signatureHeader.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Missing Typeform-Signature header"
            );
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKey = new SecretKeySpec(
                    webhookSecret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );

            mac.init(secretKey);

            byte[] digest = mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));

            String expectedSignature = "sha256="
                    + Base64.getEncoder().encodeToString(digest);

            boolean valid = MessageDigest.isEqual(
                    expectedSignature.getBytes(StandardCharsets.UTF_8),
                    signatureHeader.getBytes(StandardCharsets.UTF_8)
            );

            if (!valid) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid Typeform signature"
                );
            }

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error validating Typeform signature"
            );
        }
    }
}