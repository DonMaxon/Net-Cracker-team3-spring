package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Specification;
import com.example.demo.basicClasses.entity.User;
import com.example.demo.basicClasses.repositories.SpecificationRepository;
import com.example.demo.basicClasses.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(UUID id){
        if (!userRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public User findById(UUID id){
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
        System.out.println(user.toString());
        return user;
    }

    public List<User> getAll(){
        List<User> target = new ArrayList<>();
        userRepository.findAll().forEach(target::add);
        return target;
    }
}
