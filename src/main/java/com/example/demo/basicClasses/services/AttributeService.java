package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Attribute;


import com.example.demo.basicClasses.repositories.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;

    @Autowired
    public AttributeService(AttributeRepository attributeRepository){
        this.attributeRepository = attributeRepository;
    }

    public void save(Attribute attribute) {
        attributeRepository.save(attribute);
    }

    public void delete(UUID id){
        if (!attributeRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        attributeRepository.deleteById(id);
    }

    public Attribute findById(UUID id){
        return attributeRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteAll(){
        attributeRepository.deleteAll();
    }

}

