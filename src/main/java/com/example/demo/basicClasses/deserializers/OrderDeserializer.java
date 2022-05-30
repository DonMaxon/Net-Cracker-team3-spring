package com.example.demo.basicClasses.deserializers;

import com.example.demo.basicClasses.entity.*;
import com.example.demo.basicClasses.services.CustomerService;
import com.example.demo.basicClasses.services.LocationService;
import com.example.demo.basicClasses.services.ServiceService;
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

public class OrderDeserializer extends StdDeserializer<Order> {



    public OrderDeserializer() {
        this(null);
    }

    protected OrderDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Order deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = mapper.readTree(jsonParser.readValueAsTree().toString());
        UUID uuid = UUID.fromString(jn.get("id").asText());
        String name = jn.get("name").asText();
        String description = jn.get("description").asText();
        Order.OrderAIM aim = Order.OrderAIM.valueOf(jn.get("orderAim").asText());
        Order.OrderStatus status = Order.OrderStatus.valueOf(jn.get("orderStatus").asText());
        Specification specification = new Specification(UUID.fromString(jn.get("specificationID").asText()));
        Customer customer = null;
        if (jn.get("customerID")!=null){
            customer = new Customer(UUID.fromString(jn.get("customerID").asText()));
        }
        String js = jn.get("attributeValues").toString();
        List<AttributeValue> values;
        values = js.equals("") ? new ArrayList<>(): Arrays.asList(mapper.readValue(js, AttributeValue[].class));

        Service service = new Service(UUID.fromString(jn.get("serviceID").asText()));
        Order res = new Order(uuid, name, description, service, specification, customer,  status, aim);
        for (int i =0; i < values.size(); ++i){
            values.get(i).getAttributeValueId().setOrder(res);
        }
        res.setAttributeValues(values);
        return res;
    }
}
