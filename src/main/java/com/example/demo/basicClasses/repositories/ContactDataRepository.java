package com.example.demo.basicClasses.repositories;

import com.example.demo.basicClasses.entity.ContactData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactDataRepository extends CrudRepository<ContactData, UUID> {

}
