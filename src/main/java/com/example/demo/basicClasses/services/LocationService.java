package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.Location;
import com.example.demo.basicClasses.entity.Order;
import com.example.demo.basicClasses.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
public class LocationService {

    private final LocationRepository locationRepository;


    @Autowired
    public LocationService(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    public void save(Location location) {
        locationRepository.save(location);
    }

    public void delete(UUID id){
        if (!locationRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        locationRepository.deleteById(id);
    }

    public Location findById(UUID id){
        return locationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteAll(){
        locationRepository.deleteAll();
    }

}
