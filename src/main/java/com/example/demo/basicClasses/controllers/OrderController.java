package com.example.demo.basicClasses.controllers;

import com.example.demo.basicClasses.entity.Order;
import com.example.demo.basicClasses.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/order/delete")
    public void deleteAttribute(UUID id){
        orderService.delete(id);
    }

    @DeleteMapping("/order/get")
    public void getAttribute(UUID id){
        orderService.findById(id);
    }
    @PostMapping("/order/post")
    public void postOrder(Order order){
        orderService.save(order);
    }
}
