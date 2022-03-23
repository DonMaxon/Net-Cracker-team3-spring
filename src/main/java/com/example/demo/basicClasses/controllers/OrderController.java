package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Customer;
import com.example.demo.basicClasses.entity.Order;
import com.example.demo.basicClasses.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrderService orderService;

    /*public ResponseEntity deleteOrder(@PathVariable("id") UUID id){
        orderService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }*/

    public Order getOrder(@PathVariable("id") UUID id){
        return orderService.findById(id);
    }

    /*public ResponseEntity postOrder(@PathVariable("order") String orderString){
        try {
            Order order = Order.deserialize(orderString);
            orderService.save(order);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        catch (IOException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }*/
}
