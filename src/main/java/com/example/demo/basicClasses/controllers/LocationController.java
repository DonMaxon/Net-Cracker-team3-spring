package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Location;
import com.example.demo.basicClasses.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping("/location/delete")
    public void deleteAttribute(UUID id){
        locationService.delete(id);
    }

    @DeleteMapping("/location/get")
    public void getAttribute(UUID id){
        locationService.findById(id);
    }
    @PostMapping("/location/post")
    public void postLocation(Location location){
        locationService.save(location);
    }
}
