package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Attribute;
import com.example.demo.basicClasses.services.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("attribute")
public class AttributeController {
    @Autowired
    AttributeService attributeService;

    public ResponseEntity deleteAttribute(@PathVariable("id")  UUID id){
        attributeService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    public Attribute getAttribute(@PathVariable("id") UUID id){
        return attributeService.findById(id);
    }

}
