package com.example.demo.basicClasses.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id){
        System.out.println(id.toString());
    }

    public NotFoundException(){
        System.out.println("Object not found");
    }

}
