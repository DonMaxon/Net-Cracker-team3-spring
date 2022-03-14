package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Customer;

import com.example.demo.basicClasses.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public void delete(UUID id){
        if (!customerRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        customerRepository.deleteById(id);
    }

    public Customer findById(UUID id){
        return customerRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteAll(){
        customerRepository.deleteAll();
    }

}
