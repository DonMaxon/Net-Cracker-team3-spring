package com.example.demo.basicClasses.repositories;

import com.example.demo.basicClasses.entity.Service;
import com.example.demo.basicClasses.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    User findByLogin(String login);
}
