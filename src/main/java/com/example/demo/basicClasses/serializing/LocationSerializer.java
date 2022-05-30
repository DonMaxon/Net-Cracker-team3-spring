package com.example.demo.basicClasses.serializing;

import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.Location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LocationSerializer {


    public String serializeHierarchy(Location location) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithView(Location.LocationViews.LocationWithChildren.class).writeValueAsString(location);
    }

    public String serialize(Location location) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithView(Location.LocationViews.LocationWithParent.class).writeValueAsString(location);
    }

    public Location deserialize(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Location res = mapper.readValue(json, Location.class);
        if (res.getId()==null){
            res.setId(UUID.randomUUID());
        }
        return res;
    }
}
