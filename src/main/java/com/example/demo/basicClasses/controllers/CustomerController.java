package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/customer/delete")
    public void deleteAttribute(UUID id){
        customerService.delete(id);
    }

    @DeleteMapping("/customer/get")
    public void getAttribute(UUID id){
        customerService.findById(id);
    }
    @PostMapping("/customer/post")
    public void postCustomer(Customer customer){
        customerService.save(customer);
    }
}
