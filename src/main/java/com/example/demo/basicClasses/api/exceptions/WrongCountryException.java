package com.example.demo.basicClasses.api.exceptions;

public class WrongCountryException extends Exception {
    public WrongCountryException(){
        System.out.println("Error, Wrong Country Exception");
    }
    public WrongCountryException(String s){
        System.out.println(s);
    }
}
