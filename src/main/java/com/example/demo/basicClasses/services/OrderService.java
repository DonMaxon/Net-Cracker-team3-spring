package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.OrderServiceAPI;
import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Order;
import com.example.demo.basicClasses.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public void save(Order order) {
        if (order.getService()==null){
            orderRepository.save(new OrderServiceAPI().createOrderNew(order.getSpecification(),
                    order.getCustomer(), new ArrayList<>(order.getParams().values())));
        }
        orderRepository.save(order);
    }

    public void delete(UUID id){
        if (!orderRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        Order order = orderRepository.findById(id).get();
        if (order.getOrderAim() == Order.OrderAIM.NEW && order.getOrderStatus() == Order.OrderStatus.ENTERING){
        }
        orderRepository.deleteById(id);
    }

    public Order findById(UUID id){
        return orderRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteAll(){
        orderRepository.deleteAll();
    }


    public List<com.example.demo.basicClasses.entity.Order> findAllOrdersOfCustomer(UUID id){
        if (orderRepository.findById(id).isEmpty()){
            return null;
        }
        else{
            return orderRepository.findById(id).get().getCustomer().getOrders();
        }
    }





}