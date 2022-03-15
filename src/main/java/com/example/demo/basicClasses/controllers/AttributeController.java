package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.services.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class AttributeController {
    @Autowired
    AttributeService attributeService;

    @GetMapping("/attribute/delete")
    public void deleteAttribute(UUID id){
        attributeService.delete(id);
    }

    @DeleteMapping("/attribute/get")
    public void getAttribute(UUID id){
        attributeService.findById(id);
    }

}
