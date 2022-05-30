package com.example.demo.basicClasses.repositories;


import com.example.demo.basicClasses.entity.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpecificationRepository extends CrudRepository<Specification, UUID> {

    boolean existsByName(String name);
}
