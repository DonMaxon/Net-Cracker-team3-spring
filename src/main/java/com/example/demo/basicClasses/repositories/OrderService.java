package com.example.demo.basicClasses.repositories;

import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public void save(Order service) {
        orderRepository.save(service);
    }

    public void delete(UUID id){
        if (!orderRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        orderRepository.deleteById(id);
    }

    public Order findById(UUID id){
        return orderRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteAll(){
        orderRepository.deleteAll();
    }


    public List<com.example.demo.basicClasses.entity.Order> findAllServicesOfCustomer(UUID id){
        if (orderRepository.findById(id).isEmpty()){
            return null;
        }
        else{
            return orderRepository.findById(id).get().getCustomer().getOrders();
        }
    }
}