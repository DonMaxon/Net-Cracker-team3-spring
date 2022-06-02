package com.example.demo.basicClasses.api;



import com.example.demo.basicClasses.Repo;
import com.example.demo.basicClasses.api.exceptions.CreatingException;
import com.example.demo.basicClasses.entity.*;

import java.util.ArrayList;
import java.util.UUID;

@org.springframework.stereotype.Service
public class OrderServiceAPI {
    private static OrderServiceAPI instance;

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
        order.setSpecification(spec);
        Integer servicesSize = Repo.getInstance().getAllOrders().size();
        order.setName("New "+spec.getName()+" Order#"+servicesSize);
        servicesSize = Repo.getInstance().getAllServices().size();
        Service service = createService((spec.getName()+" Instance#"+ servicesSize), spec, customer);
        for (int i =0; i < mandatoryAttributeValues.size(); ++i){
            for (int j = 0; j < spec.getAttributes().size(); ++j){
                if (spec.getAttributes().get(j).getMandatority()){
                    order.getParams().put(spec.getAttributeIds().get(j), mandatoryAttributeValues.get(i));
                    service.getParams().put(spec.getAttributes().get(j).getId(), mandatoryAttributeValues.get(i));
                }
            }
        }

        customer.getOrders().add(order);
        order.setService(service);
        customer.getServices().add(service);
        return order;
    }

    public Order createOrderModify(Service service){
        checkOnActiveOrders(service);
        Order order = new Order(UUID.randomUUID());
        order.setAim(Order.OrderAIM.MODIFY);
        Integer num = Repo.getInstance().getAllOrders().size();
        order.setName("Modify "+service.getSpecification().getName()+" Order#"+num);
        order.setService(service);
        order.setSpecification(service.getSpecification());
        order.setCustomer(service.getCustomer());
        service.getCustomer().getOrders().add(order);
        return order;
    }

    public Order createOrderDisconnect(Service service){
        checkOnActiveOrders(service);
        Order order = new Order(UUID.randomUUID());
        order.setAim(Order.OrderAIM.DISCONNECT);
        order.setService(service);
        order.setCustomer(service.getCustomer());
        order.setSpecification(service.getSpecification());
        Integer num = Repo.getInstance().getAllOrders().size();
        order.setName("Disconnect "+service.getSpecification().getName()+" Order#"+num);
        service.getCustomer().getOrders().add(order);
        return order;
    }

    private void checkOnActiveOrders(Service service){
        for (int i = 0; i < Repo.getInstance().getAllOrders().size(); ++i){
            if (Repo.getInstance().getAllOrders().get(i).getService().equals(service) &&
                    Repo.getInstance().getAllOrders().get(i).getStatus()!= Order.OrderStatus.COMPLETED){
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
}
