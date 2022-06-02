package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.api.CustomerAPI;
import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.Order;
import com.example.demo.basicClasses.entity.Service;
import com.example.demo.basicClasses.serializing.CustomerSerializer;
import com.example.demo.basicClasses.services.CustomerService;
import com.example.demo.basicClasses.services.OrderService;
import com.example.demo.basicClasses.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerSerializer customerSerializer;
    @Autowired
    ServiceService serviceService;
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/",
            method = RequestMethod.DELETE)
    public ResponseEntity deleteCustomer(@PathVariable("id") UUID id){
        customerService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable("id") UUID id){
        return customerService.findById(id);
    }

    @RequestMapping(value = "/withoutorderservices/",
            method = RequestMethod.GET)
    public Customer getCustomerWithoutOrderServices(@PathVariable("id") UUID id){
        Customer customer = customerService.findById(id);
        CustomerAPI customerAPI = new CustomerAPI();
        return customerAPI.createCustomer(customer.getFirstName(), customer.getLastName(),
                customer.getContactData(), customer.getLocation(), customer.getAccountBalance());
    }

    @RequestMapping(value = "/",
            method = RequestMethod.POST)
    public ResponseEntity postCustomer(@RequestBody String customerString){
        try {
            Customer customer = customerSerializer.deserialize(customerString);
            List<Order> orders = customer.getOrders();
            List<Service> services = customer.getServices();
            customer.setOrders(null);
            customer.setServices(null);
            customerService.save(customer);
            for (int i = 0; i < services.size(); ++i){
                serviceService.save(services.get(i));
            }
            for (int i =0; i < orders.size(); ++i){
                orderService.save(orders.get(i));
            }
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        catch (IOException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


    }
}
