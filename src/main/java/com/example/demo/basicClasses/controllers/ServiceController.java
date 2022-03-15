package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class ServiceController {

    @Autowired
    ServiceService serviceService;

    @GetMapping("/service/delete")
    public void deleteService(UUID id){
        serviceService.delete(id);
    }

    @DeleteMapping("/service/get")
    public void getService(UUID id){
        serviceService.findById(id);
    }

}
