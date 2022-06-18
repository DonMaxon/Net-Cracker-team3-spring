package com.example.demo.basicClasses.deserializers;

import com.example.demo.basicClasses.entity.*;
import com.example.demo.basicClasses.services.CustomerService;
import com.example.demo.basicClasses.services.LocationService;
import com.example.demo.basicClasses.services.SpecificationService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ServiceDeserializer extends StdDeserializer<Service> {

    public ServiceDeserializer() {
        this(null);
    }

    protected ServiceDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Service deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = mapper.readTree(jsonParser.readValueAsTree().toString());
        UUID uuid = UUID.fromString(jn.get("id").asText());
        String name = jn.get("name").asText();
        String description = jn.get("description").toString();
        Service.ServiceStatus status = Service.ServiceStatus.valueOf(jn.asText("status").toString());
        Specification specification = new Specification(UUID.fromString(jn.get("specificationID").toString()));
        Customer customer = null;
        if (jn.get("customerID")!=null){
            customer = new Customer(UUID.fromString(jn.get("customerID").asText()));
        }
        String js = jn.get("attributeValues").toString();
        List<AttributeValue> values;
        values = js.equals("") ? new ArrayList<>(): Arrays.asList(mapper.readValue(js, AttributeValue[].class));
        Service res = new Service(uuid, name, description, status, specification, customer);
        for (int i =0; i < values.size(); ++i){
            values.get(i).setService(res);
        }
        res.setAttributeValues(values);
        return res;
    }



}
