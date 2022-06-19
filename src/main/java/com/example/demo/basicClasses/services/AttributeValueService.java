package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.*;
import com.example.demo.basicClasses.repositories.AttributeValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AttributeValueService{

    private final AttributeValueRepository attributeValueRepository;

    @Autowired
    public AttributeValueService(AttributeValueRepository attributeValueRepository){
        this.attributeValueRepository = attributeValueRepository;
    }

    public AttributeValue save(AttributeValue attributeValue) {
        return attributeValueRepository.save(attributeValue);
    }

    public void delete(UUID id){
        if (!attributeValueRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        attributeValueRepository.deleteById(id);
    }

//    public AttributeValue findById(AttributeValueId id){
//        return attributeValueRepository.findById(id).orElseThrow(NotFoundException::new);
//    }

}
