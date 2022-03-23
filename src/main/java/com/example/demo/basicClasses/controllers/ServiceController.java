package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.example.demo.basicClasses.entity.Service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("service")
public class ServiceController {

    @Autowired
    ServiceService serviceService;

    /*public ResponseEntity deleteService(@PathVariable("id")  UUID id){
        serviceService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }*/

    public Service getService(@PathVariable("id") UUID id){
        return  serviceService.findById(id);
    }

}
