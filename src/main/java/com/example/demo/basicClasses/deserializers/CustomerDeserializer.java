package com.example.demo.basicClasses.deserializers;

import com.example.demo.basicClasses.entity.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CustomerDeserializer extends StdDeserializer<Customer> {

    public CustomerDeserializer() {
        this(null);
    }

    protected CustomerDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Customer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = mapper.readTree(jsonParser.readValueAsTree().toString());
        UUID uuid = UUID.fromString(jn.get("id").asText());
        String name = jn.get("first_name").asText();
        String email = jn.get("last_name").toString();
        ContactData contactData = new ContactData(UUID.fromString(jn.get(" contactData").asText()));
        Location location = new Location(UUID.fromString(jn.get("locationID").asText()));
        Integer balance = Integer.valueOf(jn.get("accountBalance").asText());
        String js = jn.get("orders").toString();
        List<Order> orders;
        orders = js.equals("") ? new ArrayList<>(): Arrays.asList(mapper.readValue(js, Order[].class));
        List<Service> services;
        js = jn.get("services").toString();
        services = js.equals("") ? new ArrayList<>(): Arrays.asList(mapper.readValue(js, Service[].class));
        return new Customer(uuid, name, email, contactData, location, balance, orders, services);
    }
}
