package com.example.demo.basicClasses.services;

import com.example.demo.basicClasses.api.OrderServiceAPI;
import com.example.demo.basicClasses.api.exceptions.NotFoundException;
import com.example.demo.basicClasses.entity.Order;
import com.example.demo.basicClasses.repositories.OrderRepository;
import com.example.demo.basicClasses.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
        this.orderRepository = orderRepository;
    }

    public void save(Order order) {
        if (order.getService()==null){
            orderRepository.save(order);
        }
        orderRepository.save(order);
    }

    public void delete(UUID id){
        if (!orderRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        Order order = orderRepository.findById(id).get();
        if (order.getAim() == Order.OrderAIM.NEW && order.getStatus() == Order.OrderStatus.ENTERING){
            serviceRepository.deleteById(order.getService().getId());
            orderRepository.deleteById(id);

        }
        if (order.getStatus() == Order.OrderStatus.COMPLETED &&
                (order.getAim() == Order.OrderAIM.MODIFY ||
                        order.getAim() == Order.OrderAIM.DISCONNECT)) {
            orderRepository.deleteById(id);
        }
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

    public List<Order> getAll(){
        List<Order> target = new ArrayList<>();
        orderRepository.findAll().forEach(target::add);
        return target;
    }

}