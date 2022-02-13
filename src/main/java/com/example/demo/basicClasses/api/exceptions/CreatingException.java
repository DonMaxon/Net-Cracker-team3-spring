package com.example.demo.basicClasses.api.exceptions;

public class CreatingException extends RuntimeException {
    public CreatingException(){
        System.out.println("Error during create");
    }
    public CreatingException(String s){
        System.out.println(s);
    }
}
