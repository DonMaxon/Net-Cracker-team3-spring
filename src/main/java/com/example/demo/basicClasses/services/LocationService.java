package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.Location;
import com.example.demo.basicClasses.entity.Specification;
import com.example.demo.basicClasses.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final CustomerService customerService;
    private final SpecificationService specificationService;


    @Autowired
    public LocationService(LocationRepository locationRepository, CustomerService customerService,
    SpecificationService specificationService ){
        this.locationRepository = locationRepository;
        this.customerService = customerService;
        this.specificationService = specificationService;
    }

    public void save(Location location) {
        locationRepository.save(location);
    }

    public void delete(UUID id){

        if (!locationRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        List<Specification> specs = specificationService.getAll();
        List<Customer> customers = customerService.getAll();
        for (int i =0; i < customers.size(); ++i){
            if (customers.get(i).getLocation().isBelongsTo(findById(id))){
                return;
            }
        }
        for (int i =0; i < specs.size(); ++i){
            for (int j = 0; j < specs.get(i).getAvailableLocations().size(); ++j) {
                if (specs.get(i).getAvailableLocations().get(j).isBelongsTo(findById(id))){
                    return;
                }
            }
        }
        locationRepository.deleteById(id);
    }

    public Location findById(UUID id){
        return locationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteAll(){

        locationRepository.deleteAll();
    }

    public List<Location> getAll(){
        List<Location> target = new ArrayList<>();
        locationRepository.findAll().forEach(target::add);
        return target;
    }

    public List<Location> findByType(Location.Types type){
        return locationRepository.findByType(type);
    }


}
