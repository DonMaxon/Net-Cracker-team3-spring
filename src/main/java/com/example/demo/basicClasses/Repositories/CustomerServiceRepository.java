package com.example.demo.basicClasses.Repositories;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceRepository {

    private final CustomerRepository customerRepository;

    private List<Customer> customers;

    @Autowired
    public CustomerServiceRepository(CustomerRepository customerRepository){
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
        if (customerRepository.findById(id).isEmpty()) {
            return null;
        }
        return customerRepository.findById(id).get();
    }

    public void deleteAll(Customer customer){
        customerRepository.deleteAll();
    }

    public List<Order> findAllOrdersOfCustomer(UUID id){
        if (customerRepository.findById(id).isEmpty()){
            return null;
        }
        else{
            return customerRepository.findById(id).get().getOrders();
        }
    }

    public List<com.example.demo.basicClasses.entity.Service> findAllServicesOfCustomer(UUID id){
        if (customerRepository.findById(id).isEmpty()){
            return null;
        }
        else{
            return customerRepository.findById(id).get().getServices();
        }
    }


}
