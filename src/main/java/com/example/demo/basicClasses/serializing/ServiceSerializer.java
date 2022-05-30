package com.example.demo.basicClasses.serializing;

import com.example.demo.basicClasses.entity.AttributeValue;
import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.OrderAndServiceViews;
import com.example.demo.basicClasses.entity.Service;
import com.example.demo.basicClasses.services.CustomerService;
import com.example.demo.basicClasses.services.SpecificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class ServiceSerializer {
    @Autowired
    CustomerService customerService;
    @Autowired
    SpecificationService specificationService;

    public String serialize(Service service) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithView(AttributeValue.ValueViews.ValueWithoutOrderService.class).writeValueAsString(service);
    }

    public Service deserialize(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Service service = mapper.readValue(json, Service.class);
        service.setCustomer(customerService.findById(service.getCustomer().getId()));
        service.setSpecification(specificationService.findById(service.getSpecification().getId()));
        return service;
    }

}
