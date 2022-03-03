package com.example.demo.basicClasses.repositories;

import com.example.demo.basicClasses.entity.Attribute;
import com.example.demo.basicClasses.entity.ContactData;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AttributeRepository extends CrudRepository<Attribute, UUID> {
}
