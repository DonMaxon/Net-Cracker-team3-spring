package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;

import com.example.demo.basicClasses.entity.Order;
import com.example.demo.basicClasses.entity.Service;
import com.example.demo.basicClasses.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }

    public void save(Service service) {
        serviceRepository.save(service);
    }

    public void delete(UUID id){
        if (!serviceRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        serviceRepository.deleteById(id);
    }

    public Service findById(UUID id){
        return serviceRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteAll(){
        serviceRepository.deleteAll();
    }


    public List<com.example.demo.basicClasses.entity.Service> findAllServicesOfCustomer(UUID id){
        if (serviceRepository.findById(id).isEmpty()){
            return null;
        }
        else{
            return serviceRepository.findById(id).get().getCustomer().getServices();
        }
    }

    public List<Service> getAll(){
        List<Service> target = new ArrayList<>();
        serviceRepository.findAll().forEach(target::add);
        return target;
    }
}
