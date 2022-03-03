package com.example.demo.basicClasses.repositories;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Customer;

import com.example.demo.basicClasses.entity.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.UUID;

@Service
public class SpecificationService {

    private final SpecificationRepository specificationRepository;

    @Autowired
    public SpecificationService(SpecificationRepository specificationRepository){
        this.specificationRepository = specificationRepository;
    }

    public void save(Specification specification) {
        specificationRepository.save(specification);
    }

    public void delete(UUID id){
        if (!specificationRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        specificationRepository.deleteById(id);
    }

    public Specification findById(UUID id){
        return specificationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteAll(){
        specificationRepository.deleteAll();
    }

}