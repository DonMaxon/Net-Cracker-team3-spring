package com.example.demo.basicClasses.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
public class CreatingException extends RuntimeException {
    public CreatingException(){
        System.out.println("Error during create");
    }
    public CreatingException(String s){
        System.out.println(s);
    }
}
