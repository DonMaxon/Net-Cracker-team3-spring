package com.example.demo.basicClasses.serializing;

import com.example.demo.basicClasses.entity.*;
import com.example.demo.basicClasses.services.CustomerService;
import com.example.demo.basicClasses.services.ServiceService;
import com.example.demo.basicClasses.services.SpecificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderSerializer {

    @Autowired
    CustomerService customerService;
    @Autowired
    SpecificationService specificationService;
    @Autowired
    ServiceService serviceService;

    public String serialize(Order order) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithView(AttributeValue.ValueViews.ValueWithoutOrderService.class).writeValueAsString(order);
    }

    public Order deserialize(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Order res = mapper.readValue(json, Order.class);
        res.setService(serviceService.findById(res.getService().getId()));
        res.setCustomer(customerService.findById(res.getCustomer().getId()));
        res.setSpecification(specificationService.findById(res.getSpecification().getId()));
        return res;
    }
}
