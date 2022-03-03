package com.example.demo.basicClasses.repositories;


import com.example.demo.basicClasses.entity.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocationRepository extends CrudRepository<Location, UUID> {
}
