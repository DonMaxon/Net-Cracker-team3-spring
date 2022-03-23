package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    public ResponseEntity deleteCustomer(@PathVariable("id") UUID id){
        customerService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    public Customer getCustomer(@PathVariable("id") UUID id){
        return customerService.findById(id);
    }

    public ResponseEntity postCustomer(@PathVariable("customer") String customerString){
        try {
            Customer customer = Customer.deserialize(customerString);
            customerService.save(customer);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        catch (IOException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


    }
}
