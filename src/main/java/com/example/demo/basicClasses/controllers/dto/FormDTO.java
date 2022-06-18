package com.example.demo.basicClasses.controllers.dto;

import java.util.ArrayList;
import java.util.List;

public class FormDTO {
    private List<FieldDTO> fields = new ArrayList<>();

    public List<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<FieldDTO> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "FormDTO{" +
                "fields=" + fields +
                '}';
    }
}
