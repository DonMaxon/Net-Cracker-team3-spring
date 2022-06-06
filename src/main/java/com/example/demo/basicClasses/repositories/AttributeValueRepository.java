package com.example.demo.basicClasses.repositories;

import com.example.demo.basicClasses.entity.Attribute;
import com.example.demo.basicClasses.entity.AttributeValue;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AttributeValueRepository extends CrudRepository<AttributeValue, UUID> {
}
