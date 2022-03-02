package com.example.demo.basicClasses.api.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id){
        System.out.println(id.toString());
    }

    public NotFoundException(){
        System.out.println("Object not found");
    }

}
