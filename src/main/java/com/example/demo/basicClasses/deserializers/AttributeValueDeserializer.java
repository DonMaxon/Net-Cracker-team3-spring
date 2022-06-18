package com.example.demo.basicClasses.deserializers;

import com.example.demo.basicClasses.entity.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class AttributeValueDeserializer extends StdDeserializer<AttributeValue> {

    public AttributeValueDeserializer() {
        this(null);
    }

    protected AttributeValueDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public AttributeValue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = mapper.readTree(jsonParser.readValueAsTree().toString());
        UUID attrid = UUID.fromString(jn.get("attributeId").asText());

        UUID orderid = null;
        try {
            orderid = UUID.fromString(jn.get("orderId").asText());
        }
        catch (Exception e){

        }
        UUID serviceid = null;
        try {
            serviceid = UUID.fromString(jn.get("serviceId").asText());
        }
        catch (Exception e){

        }
        String val = jn.get("value").asText();


        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setOrder( new Order(orderid));
        attributeValue.setService( new Service(serviceid));
        attributeValue.setAttribute(new Attribute(attrid));
        return attributeValue;
    }
}
