package com.example.demo.basicClasses.deserializers;

import com.example.demo.basicClasses.entity.Attribute;
import com.example.demo.basicClasses.entity.Location;
import com.example.demo.basicClasses.entity.Specification;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SpecificationDeserializer extends StdDeserializer<Specification> {

    public SpecificationDeserializer() {
        this(null);
    }

    protected SpecificationDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Specification deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = mapper.readTree(jsonParser.readValueAsTree().toString());
        UUID uuid = UUID.fromString(jn.get("id").asText());
        String name = jn.get("name").asText();
        String description = jn.get("description").asText();
        String js = jn.get("attributes").toString();
        List<Attribute> attributes;
        attributes = js.equals("") ? new ArrayList<>(): new ArrayList<>(Arrays.asList(mapper.readValue(js, Attribute[].class)));
        List<String> locationIds;
        js = jn.get("availableLocationId").toString();
        locationIds = js.equals("") ? new ArrayList<>(): new ArrayList<>
                (Arrays.asList(mapper.readValue(js, String[].class)));
        List<Location> locations = new ArrayList<>();
        for (int i =0; i < locationIds.size(); ++i){
            locations.add(new Location(UUID.fromString(locationIds.get(i))));
        }
        return new Specification(uuid, name, description, attributes, locations);
    }

}
