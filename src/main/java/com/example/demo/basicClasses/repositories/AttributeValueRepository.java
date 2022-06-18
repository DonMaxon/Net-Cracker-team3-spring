package com.example.demo.basicClasses.repositories;

import com.example.demo.basicClasses.entity.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttributeValueRepository extends CrudRepository<AttributeValue, UUID> {
}
