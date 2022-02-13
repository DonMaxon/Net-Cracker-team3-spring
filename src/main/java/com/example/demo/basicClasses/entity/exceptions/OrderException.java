package com.example.demo.basicClasses.entity.exceptions;

public class OrderException extends RuntimeException {
    public OrderException(){
        System.out.println("Error, Order Exception");
    }
    public OrderException(String s){
        System.out.println(s);
    }
}
