package com.example.demo.basicClasses.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.persistence.Entity;
import java.util.Objects;
import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeObject {
    private OrderService object;
    private Attribute attribute;
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeObject that = (AttributeObject) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(id, that.id);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attribute);
    }

    @Override
    public String toString() {
        return "AttributeObject{" +
                "uniqueId=" + id +
                ", attribute=" + attribute +
                '}';
    }

    public String serialize() throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }
}
