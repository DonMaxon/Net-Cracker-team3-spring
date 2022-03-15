package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.services.ContactDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class ContactDataController {

    @Autowired
    ContactDataService contactDataService;

    @GetMapping("/contactData/delete")
    public void deleteAttribute(UUID id){
        contactDataService.delete(id);
    }

    @DeleteMapping("/contactData/get")
    public void getAttribute(UUID id){
        contactDataService.findById(id);
    }
}
