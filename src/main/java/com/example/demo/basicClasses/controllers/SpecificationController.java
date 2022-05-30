package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.serializing.ServiceSerializer;
import com.example.demo.basicClasses.serializing.SpecificationSerializer;
import com.example.demo.basicClasses.services.OrderService;
import com.example.demo.basicClasses.entity.Specification;
import com.example.demo.basicClasses.services.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Autowired
    SpecificationService specificationService;
    @Autowired
    OrderService orderService;
    @Autowired
    SpecificationSerializer specificationSerializer;

    @RequestMapping(value = "/",
            method = RequestMethod.DELETE)
    public ResponseEntity deleteSpecification(@PathVariable("id") UUID id){
        specificationService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/",
            method = RequestMethod.GET)
    public Specification getSpecification(@PathVariable("id") UUID id){
        return specificationService.findById(id);
    }

    @RequestMapping(value = "/",
            method = RequestMethod.POST)
    public ResponseEntity postSpecification(@RequestBody String specificationString){
        try {
            Specification specification = specificationSerializer.deserialize(specificationString);
            specification = specificationService.save(specification);
            return new ResponseEntity( specification, HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace(System.out);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        
    }

}
