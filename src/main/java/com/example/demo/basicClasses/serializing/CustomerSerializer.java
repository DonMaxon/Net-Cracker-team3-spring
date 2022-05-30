package com.example.demo.basicClasses.serializing;

import com.example.demo.basicClasses.entity.ContactData;
import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.OrderAndServiceViews;
import com.example.demo.basicClasses.services.LocationService;
import com.example.demo.basicClasses.services.SpecificationService;
import com.example.demo.basicClasses.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CustomerSerializer {

    @Autowired
    LocationService locationService;
    @Autowired
    SpecificationService specificationService;
    @Autowired
    UserService userService;


    public String serialize(Customer customer) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithView(OrderAndServiceViews.WithoutCustomerID.class).withDefaultPrettyPrinter().writeValueAsString(customer);
    }

    public Customer deserialize(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Customer res = mapper.readValue(json, Customer.class);
        res.setLocation(locationService.findById(res.getLocation().getId()));
        res.setUser(userService.findById(res.getUserID()));
        if (res.getId()==null){
            res.setId(UUID.randomUUID());
        }
        for (int i =0; i < res.getServices().size();++i){
            res.getServices().get(i).setCustomer(res);
            res.getServices().get(i).setSpecification(
                    specificationService.findById(res.getServices().get(i).getSpecification().getId()));
        }
        for (int i =0; i < res.getOrders().size();++i){
            res.getOrders().get(i).setCustomer(res);
            res.getOrders().get(i).setSpecification(
                    specificationService.findById(res.getOrders().get(i).getSpecification().getId()));
            for (int j =0; j < res.getServices().size(); ++j){
                if(res.getOrders().get(i).getService().getId().
                        equals(res.getServices().get(j).getId())){
                    res.getOrders().get(i).setService(res.getServices().get(j));
                }
            }
        }

        return  res;
    }
}
