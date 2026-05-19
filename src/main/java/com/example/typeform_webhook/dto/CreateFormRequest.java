package com.example.typeform_webhook.dto;

import java.util.List;

public class CreateFormRequest {
     private String title;
    private List<FieldRequest> fields;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FieldRequest> getFields() {
        return fields;
    }

    public void setFields(List<FieldRequest> fields) {
        this.fields = fields;
    }
}
