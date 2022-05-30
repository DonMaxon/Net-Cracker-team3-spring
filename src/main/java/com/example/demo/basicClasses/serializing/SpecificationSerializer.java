package com.example.demo.basicClasses.serializing;

import com.example.demo.basicClasses.entity.Attribute;
import com.example.demo.basicClasses.entity.Specification;
import com.example.demo.basicClasses.services.LocationService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SpecificationSerializer {

    @Autowired
    LocationService locationService;

    public String serialize(Specification specification) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithView(Attribute.AttributeViews.AttributeWithoutParent.class).writeValueAsString(specification);
    }

    public Specification deserialize(String json) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Specification res = mapper.readValue(json, Specification.class);
        if (res.getId()==null){
            res.setId(UUID.randomUUID());
        }
        for (int i =0; i < res.getAvailableLocations().size(); ++i){
            res.getAvailableLocations().set(i,
                    locationService.findById(res.getAvailableLocations().get(i).getId()));
        }
        for (int i = 0; i < res.getAttributes().size(); ++i){
            if (res.getAttributes().get(i).getId()==null){
                res.getAttributes().get(i).setId(UUID.randomUUID());
            }
            res.getAttributes().get(i).setSpecification(res);
        }
        return res;
    }
}
