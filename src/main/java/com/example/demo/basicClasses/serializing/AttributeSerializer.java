package com.example.demo.basicClasses.serializing;

import com.example.demo.basicClasses.entity.Attribute;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class AttributeSerializer {

    public String serializeWithParent(Attribute attribute) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithView(Attribute.AttributeViews.AttributeWithParent.class).writeValueAsString(attribute);
    }

    public String serializeWithoutParent(Attribute attribute) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithView(Attribute.AttributeViews.AttributeWithoutParent.class).writeValueAsString(attribute);
    }

    public Attribute deserialize(String json) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Attribute.class);
    }

}
