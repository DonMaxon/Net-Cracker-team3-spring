package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Attribute;
import com.example.demo.basicClasses.entity.AttributeValue;
import com.example.demo.basicClasses.entity.AttributeValueId;
import com.example.demo.basicClasses.repositories.AttributeRepository;
import com.example.demo.basicClasses.repositories.AttributeValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AttributeValueService {

    private final AttributeValueRepository attributeValueRepository;

    @Autowired
    public AttributeValueService(AttributeValueRepository attributeValueRepository){
        this.attributeValueRepository = attributeValueRepository;
    }

    public void save(AttributeValue attributeValue) {
        attributeValueRepository.save(attributeValue);
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

    public void deleteAll(){
        attributeValueRepository.deleteAll();
    }
}
