package com.example.demo.basicClasses.repositories;


import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.Location;
import com.example.demo.basicClasses.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    Customer findByUser(User user);
}
