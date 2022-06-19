package com.example.demo.basicClasses.api;



import com.example.demo.basicClasses.api.exceptions.CreatingException;
import com.example.demo.basicClasses.entity.*;
import com.example.demo.basicClasses.services.AttributeValueService;
import com.example.demo.basicClasses.services.OrderService;
import com.example.demo.basicClasses.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class OrderServiceAPI {

    @Autowired
    OrderService orderService;
    @Autowired
    ServiceService serviceService;
    @Autowired
    AttributeValueService attributeValueService;


    private Service createService(String name, Specification spec, Customer customer) {
        Service service = new Service(UUID.randomUUID(), name, Service.ServiceStatus.PLANNED, spec, customer);
        return service;
    }

    public Order createOrderNew(Specification spec, Customer customer,
                                ArrayList<AttributeValue> mandatoryAttributeValues){
        if (!isValidLocation(customer, spec)){
            throw new CreatingException("Error during creating new order, location is not valid");
        }
        Order order = new Order(UUID.randomUUID());
        order.setCustomer(customer);
        order.setAim(Order.OrderAIM.NEW);
        order.setOrderStatus(Order.OrderStatus.ENTERING);
        order.setSpecification(spec);
        Integer servicesSize = orderService.getAll().size() + 1;
        order.setName("New "+spec.getName()+" Order #"+servicesSize);
        servicesSize = serviceService.getAll().size() + 1;
        Service service = createService((spec.getName()+" Instance #"+ servicesSize), spec, customer);

        for (int i =0; i < mandatoryAttributeValues.size(); ++i){
            for (int j = 0; j < spec.getAttributes().size(); ++j){
                if (spec.getAttributes().get(j).getMandatority()){
                    order.getParams().put(spec.getAttributeIds().get(j), mandatoryAttributeValues.get(i));
                    service.getParams().put(spec.getAttributes().get(j).getId(), mandatoryAttributeValues.get(i));
                }
            }
        }
        service = serviceService.save(service);
        order.setService(service);
        return orderService.save(order);
    }

    public Order createOrderModify(Service service){
        checkOnActiveOrders(service);
        Order order = new Order(UUID.randomUUID());
        order.setAim(Order.OrderAIM.MODIFY);
        Integer num = orderService.getAll().size() + 1;
        order.setName("Modify "+service.getSpecification().getName()+" Order #"+num);
        order.setService(service);
        order.setSpecification(service.getSpecification());
        order.setCustomer(service.getCustomer());
        order = orderService.save(order);
        List<AttributeValue> values = new ArrayList<>();
        for(AttributeValue serviceValue : service.getAttributeValues()){
            AttributeValue orderValue = new AttributeValue(UUID.randomUUID());
            orderValue.setOrder(order);
            orderValue.setAttribute(serviceValue.getAttribute());
            orderValue.setValue(serviceValue.getValue());
            values.add(attributeValueService.save(orderValue));
        }
        order.setAttributeValues(values);
        return orderService.save(order);
    }

    public Order createOrderDisconnect(Service service){
        checkOnActiveOrders(service);
        Order order = new Order(UUID.randomUUID());
        order.setAim(Order.OrderAIM.DISCONNECT);
        order.setService(service);
        order.setCustomer(service.getCustomer());
        order.setSpecification(service.getSpecification());
        Integer num = orderService.getAll().size() + 1;
        order.setName("Disconnect "+service.getSpecification().getName()+" Order #"+num);
        order = orderService.save(order);
        List<AttributeValue> values = new ArrayList<>();
        for(AttributeValue serviceValue : service.getAttributeValues()){
            AttributeValue orderValue = new AttributeValue(UUID.randomUUID());
            orderValue.setOrder(order);
            orderValue.setAttribute(serviceValue.getAttribute());
            orderValue.setValue(serviceValue.getValue());
            values.add(attributeValueService.save(orderValue));
        }
        order.setAttributeValues(values);
        return orderService.save(order);
    }

    private void checkOnActiveOrders(Service service){
        List<Order> orders = orderService.getAll();
        for (int i = 0; i < orders.size(); ++i){
            if (orders.get(i).getService().equals(service) &&
                    orders.get(i).getStatus()!= Order.OrderStatus.COMPLETED){
                throw new CreatingException("Service has active orders");
            }
        }
    }

    private boolean isValidLocation (Customer customer, Specification spec){
        if (spec.getAvailableLocations().size()==0){
            return true;
        }
        for (int i = 0; i < spec.getAvailableLocations().size(); ++i){
            if (customer.getLocation().isBelongsTo(spec.getAvailableLocations().get(i))){
                return true;
            }
        }

        return false;
    }

    public void startOrder(Order order) {
        order.startOrder();
        List<Attribute> attributes = order.getSpecification().getMandatoryAttribute();
        for(Attribute attribute : attributes){
            if(!order.getParams().containsKey(attribute.getId())){
                throw new IllegalStateException();
            }
        }
        serviceService.save(order.getService());
        orderService.save(order);
    }

    public void completeOrder(Order order) {
        order.completeOrder();
        Service service = order.getService();
        List<AttributeValue> values = new ArrayList<>();
        for(AttributeValue orderValue : order.getAttributeValues()){
            AttributeValue serviceValue = service.getParams().containsKey(orderValue.getAttributeId()) ?
                    service.getParams().get(orderValue.getAttributeId())
                    : new AttributeValue(UUID.randomUUID());
            serviceValue.setService(service);
            serviceValue.setAttribute(orderValue.getAttribute());
            serviceValue.setValue(orderValue.getValue());
            System.out.println(serviceValue);
            values.add(attributeValueService.save(serviceValue));
        }
        order.getService().setValues(values);
        serviceService.save(order.getService());
        orderService.save(order);
    }
}
