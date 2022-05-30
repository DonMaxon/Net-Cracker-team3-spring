package com.example.demo.basicClasses.deserializers;

import com.example.demo.basicClasses.entity.Attribute;
import com.example.demo.basicClasses.entity.ContactData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.UUID;

public class ContactDataDeserializer extends StdDeserializer<ContactData> {

    public ContactDataDeserializer() {
        this(null);
    }

    protected ContactDataDeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public ContactData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = mapper.readTree(jsonParser.readValueAsTree().toString());
        UUID uuid = UUID.fromString(jn.get("id").asText());
        String name = jn.get("name").asText();
        String email = jn.get("email").toString();
        return new ContactData(uuid, name, email);
    }
}
