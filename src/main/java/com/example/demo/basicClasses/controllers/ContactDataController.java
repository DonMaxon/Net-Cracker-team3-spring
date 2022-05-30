package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.ContactData;
import com.example.demo.basicClasses.services.ContactDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/contactData")
public class ContactDataController {

    @Autowired
    ContactDataService contactDataService;

    @RequestMapping(value = "/",
            method = RequestMethod.DELETE)
    public ResponseEntity deleteContactData(@PathVariable("id") UUID id){
        contactDataService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/",
            method = RequestMethod.GET)
    public ContactData getContactData(@PathVariable("id") UUID id){
        return contactDataService.findById(id);
    }
}
