package com.example.demo.basicClasses.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class WrongCountryException extends Exception {
    public WrongCountryException(){
        System.out.println("Error, Wrong Country Exception");
    }
    public WrongCountryException(String s){
        System.out.println(s);
    }
}
