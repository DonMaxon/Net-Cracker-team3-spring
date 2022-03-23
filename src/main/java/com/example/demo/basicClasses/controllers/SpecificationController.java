package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Customer;
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

@Controller
@RequestMapping("specification")
public class SpecificationController {

    @Autowired
    SpecificationService specificationService;
    @Autowired
    OrderService orderService;

    public ResponseEntity deleteSpecification(@PathVariable("id") UUID id){
        specificationService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    public Specification getSpecification(@PathVariable("id") UUID id){
        return specificationService.findById(id);
    }

    public ResponseEntity postSpecification(@PathVariable("specificaion") String specificationString){
        try {
            Specification specification = Specification.deserialize(specificationString);
            specificationService.save(specification);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        catch (IOException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

}
