package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.Location;
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
@RequestMapping("location")
public class LocationController {

    @Autowired
    LocationService locationService;

    public ResponseEntity deleteLocation(@PathVariable("id") UUID id){
        locationService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    public Location getLocation(@PathVariable("id") UUID id){
        return locationService.findById(id);
    }

    public ResponseEntity postLocation(@PathVariable("location") String locationString){
        try {
            List<Location> locations = Location.deserialize(locationString, null);
            for (int i =0; i < locations.size(); ++i) {
                locationService.save(locations.get(i));
            }
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        catch (IOException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
