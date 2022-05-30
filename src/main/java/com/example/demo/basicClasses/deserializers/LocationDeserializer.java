package com.example.demo.basicClasses.deserializers;

import com.example.demo.basicClasses.entity.Attribute;
import com.example.demo.basicClasses.entity.ContactData;
import com.example.demo.basicClasses.entity.Location;
import com.example.demo.basicClasses.entity.Order;
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

public class LocationDeserializer extends StdDeserializer<Location> {

    public LocationDeserializer() {
        this(null);
    }

    protected LocationDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Location deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = mapper.readTree(jsonParser.readValueAsTree().toString());
        UUID uuid;
        if (jn.get("id")!=null){
            uuid = UUID.fromString(jn.get("id").asText());
        }
        else{
            uuid = UUID.randomUUID();
        }
        String name = jn.get("name").asText();
        Location.Types type = Location.Types.valueOf(jn.get("type").asText());
        Location parent = null;
        if (jn.get("parentID")!=null){
            parent = new Location(UUID.fromString(jn.get("parentID").asText()));
        }
        Location res = new Location(uuid, name, type, parent);
        try {
            String js = jn.get("children").toString();
            List<Location> children;
            children = js.equals("") ? new ArrayList<>() : Arrays.asList(mapper.readValue(js, Location[].class));
            for (Location child : children) {
                child.setParent(res);
            }
            res.setChildren(children);
        }
        catch (NullPointerException e){

        }
        return res;
    }
}
