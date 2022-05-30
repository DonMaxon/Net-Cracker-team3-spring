package com.example.demo.basicClasses.deserializers;

import com.example.demo.basicClasses.entity.Attribute;
import com.example.demo.basicClasses.entity.Specification;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AttributeDeserializer extends StdDeserializer<Attribute> {

    public AttributeDeserializer() {
        this(null);
    }

    protected AttributeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Attribute deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = mapper.readTree(jsonParser.readValueAsTree().toString());
        UUID uuid = UUID.fromString(jn.get("id").asText());
        Boolean mandatority = Boolean.valueOf(jn.get("isMandatory").asText());
        Attribute.AttributeTypes type = Attribute.AttributeTypes.valueOf(jn.get("type").asText());
        String name = jn.get("name").asText();
        Specification specification = new Specification(UUID.fromString(jn.get("specificationId").asText()));
        return new Attribute(uuid, name, mandatority, type, specification);
    }
}
