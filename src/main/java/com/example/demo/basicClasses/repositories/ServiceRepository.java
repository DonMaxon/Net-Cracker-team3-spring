package com.example.demo.basicClasses.repositories;

import com.example.demo.basicClasses.entity.Customer;

import com.example.demo.basicClasses.entity.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceRepository extends CrudRepository<Service, UUID> {

}
