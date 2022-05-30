package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.Location;
import com.example.demo.basicClasses.serializing.LocationSerializer;
import com.example.demo.basicClasses.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/location")
public class LocationController {

    @Autowired
    LocationService locationService;
    @Autowired
    LocationSerializer locationSerializer;

    @RequestMapping(value = "/",
            method = RequestMethod.DELETE)
    public ResponseEntity deleteLocation(@PathVariable("id") UUID id){
        locationService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/",
            method = RequestMethod.GET)
    public Location getLocation(@PathVariable("id") UUID id){
        return locationService.findById(id);
    }

    @RequestMapping(value = "/",
            method = RequestMethod.POST)
    public ResponseEntity postOneLocation(@RequestBody String locationString){
        try {
            Location location = locationSerializer.deserialize(locationString);
            locationService.save(location);
            return new ResponseEntity(location, HttpStatus.ACCEPTED);
        }
        catch (IOException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


}
