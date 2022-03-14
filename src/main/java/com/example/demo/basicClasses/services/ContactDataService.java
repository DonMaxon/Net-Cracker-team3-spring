package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.ContactData;

import com.example.demo.basicClasses.repositories.ContactDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.UUID;

@Service
public class ContactDataService {

    private final ContactDataRepository contactDataRepository;

    @Autowired
    public ContactDataService(ContactDataRepository contactDataRepository){
        this.contactDataRepository = contactDataRepository;
    }

    public void save(ContactData contactData) {
        contactDataRepository.save(contactData);
    }

    public void delete(UUID id){
        if (!contactDataRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        contactDataRepository.deleteById(id);
    }

    public ContactData findById(UUID id){
        return contactDataRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteAll(){
        contactDataRepository.deleteAll();
    }

}

