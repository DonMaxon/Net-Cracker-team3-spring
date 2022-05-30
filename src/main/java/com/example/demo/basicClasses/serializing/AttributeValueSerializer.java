package com.example.demo.basicClasses.serializing;

import com.example.demo.basicClasses.entity.Attribute;
import com.example.demo.basicClasses.entity.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class AttributeValueSerializer {

    public String serialize(AttributeValue attributeValue) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithView(AttributeValue.ValueViews.ValueWithOrderService.class).writeValueAsString(attributeValue);
    }

    public AttributeValue deserialize(String json) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, AttributeValue.class);
    }


}
