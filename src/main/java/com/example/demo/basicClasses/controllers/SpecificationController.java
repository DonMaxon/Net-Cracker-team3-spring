package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Specification;
import com.example.demo.basicClasses.services.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class SpecificationController {

    @Autowired
    SpecificationService specificationService;

    @GetMapping("/specification/delete")
    public void deleteAttribute(UUID id){
        specificationService.delete(id);
    }

    @DeleteMapping("/specification/get")
    public void getAttribute(UUID id){
        specificationService.findById(id);
    }
    @PostMapping("/specification/post")
    public void postService(Specification customer){
        specificationService.save(customer);
    }

}
