package com.example.demo.basicClasses.entity;


import com.example.demo.basicClasses.Repo;

import com.example.demo.basicClasses.entity.exceptions.OrderException;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.stereotype.Component;


import javax.persistence.*;
import java.io.IOException;
import java.util.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "Orders")
public class Order implements OrderService, ObjectWithId {

    public enum OrderStatus  {ENTERING, IN_PROGRESS, COMPLETED};
    public enum OrderAIM{NEW, MODIFY, DISCONNECT};

    @Id
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "specification_id")
    private Specification specification;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "aim")
    private OrderAIM aim;

    @JsonDeserialize(keyUsing = Attribute.AttributeDeserializer.class)
    @Transient
    private Map<UUID, AttributeValue> params;

    @Transient
    private List<AttributeValue> values;

    public Order() {
    }

    public Order(UUID id){
        this.id = id;
        status = OrderStatus.ENTERING;
        params = new HashMap<>();
    }


    public Order(UUID id, String name, Service serviceId, Specification specification, Customer customer, OrderStatus status, OrderAIM aim) {
        this.id = id;
        this.name = name;
        this.service = serviceId;
        this.specification = specification;
        this.customer = customer;
        this.status = status;
        this.aim = aim;
        params = new HashMap<>();
    }

    public Order(UUID id, String name, String prescription, Service serviceId,
                 Specification specification, Customer customer, OrderStatus status, OrderAIM aim, Map<UUID, AttributeValue> params) {
        this.id = id;
        this.name = name;
        this.description = prescription;
        this.service = serviceId;
        this.specification = specification;
        this.customer = customer;
        this.status = status;
        this.aim = aim;
        this.params=params;
    }

    public Map<UUID, AttributeValue> getParams() {
        if (status==OrderStatus.ENTERING) {
            return params;
        }
        return new HashMap<>(params);
    }

    public void setParams(Map<UUID, AttributeValue> params) {
        if (status==OrderStatus.ENTERING||status==null) {
            this.params = params;
        }

    }



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public OrderStatus getOrderStatus() {
        return status;
    }

    public void startOrder(){
        if (status==OrderStatus.ENTERING){
            setOrderStatus(OrderStatus.IN_PROGRESS);
        }
        else{
            throw new OrderException("Can not start order, status is not entering");
        }

    }

    public void completeOrder(){
        if (status==OrderStatus.IN_PROGRESS){
            setOrderStatus(OrderStatus.COMPLETED);
        }
        else{
            throw new OrderException("Can not start order, status is not in progress");
        }

    }

    private void setOrderStatus(OrderStatus status) {
        if (status == OrderStatus.COMPLETED&&aim==OrderAIM.NEW){
            service.setStatus(Service.ServiceStatus.ACTIVE);
        }
        if (status == OrderStatus.COMPLETED&&aim==OrderAIM.DISCONNECT){
            service.setStatus(Service.ServiceStatus.DISCONNECTED);
        }
        if (status == OrderStatus.COMPLETED&&aim!=OrderAIM.DISCONNECT){
            for (Map.Entry entryOrder: params.entrySet()){
                for (Map.Entry entryService: service.getParams().entrySet()){
                    if (entryOrder.getKey().equals(entryService.getKey())){
                        entryService.setValue(entryOrder.getValue());
                    }
                }
            }
        }
        this.status = status;
    }


    public OrderAIM getOrderAim() {
        return aim;
    }


    public void setOrderAim(OrderAIM aim) {
        this.aim = aim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(name, order.name) &&
                Objects.equals(description, order.description) &&
                Objects.equals(service, order.service) &&
                Objects.equals(specification, order.specification) &&
                Objects.equals(customer.getId(), order.customer.getId()) &&
                status == order.status &&
                aim == order.aim&&
                Objects.equals(params, order.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, service, specification, customer, status, aim, params);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", service=" + service +
                ", specification=" + specification +
                ", customer=" + customer.getId().toString() +
                ", status=" + status +
                ", aim=" + aim +
                ", params=" + params +
                '}';
    }



    public String serialize() throws JsonProcessingException{
        return new ObjectMapper().writeValueAsString(this);
    }



    public static Order deserialize(String str) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(str, Order.class);
        for(Map.Entry<UUID, AttributeValue> entry: order.getParams().entrySet()){
            String val;
            switch(Specification.findAttributeById(entry.getKey()).getType()){
                case DATE: val = entry.getValue().getValue();
                    entry.setValue(new AttributeValue());
                    entry.getValue().setType(Attribute.AttributeTypes.DATE);
                    entry.getValue().setValue(val);
                    break;
                case NUMBER: val = entry.getValue().getValue();
                    entry.setValue(new AttributeValue());
                    entry.getValue().setType(Attribute.AttributeTypes.NUMBER);
                    entry.getValue().setValue(val);
                    break;
                case STRING:
                    entry.getValue().setType(Attribute.AttributeTypes.STRING);
            }
        }
        return order;
    }




    @JsonGetter
    private UUID getSpecificationID(){
        return specification.getId();
    }

    @JsonSetter
    private void setSpecificationID(UUID specID){
        Repo repo = Repo.getInstance();
        if (repo!=null&&repo.getSpecs()!=null) {
            for (int i = 0; i < repo.getSpecs().size(); ++i) {
                if (repo.getSpecs().get(i).getId() != null &&
                        repo.getSpecs().get(i).getId().equals(specID))
                    specification = repo.getSpecs().get(i);
                return;
            }
        }
        specification=new Specification(specID);
    }

    @JsonGetter
    private UUID getServiceID(){
        return service.getId();
    }

    @JsonSetter
    private void setServiceID(UUID specID){
        Repo repo = Repo.getInstance();
        if (repo!=null&&repo.getCustomers()!=null) {
            ArrayList<Service> allServices = repo.getAllServices();
            for (int i = 0; i < allServices.size(); ++i) {
                if (allServices.get(i).getId() != null &&
                        allServices.get(i).getId().equals(specID))
                    service = allServices.get(i);
                return;
            }
        }
        service=new Service(specID);
    }

    @JsonGetter
    @JsonView(OrderAndServiceViews.WithCustomerID.class)
    private UUID getCustomerID(){
        return customer.getId();
    }

    @JsonSetter
    @JsonView(OrderAndServiceViews.WithCustomerID.class)
    private void setCustomerID(UUID specID){
        Repo repo = Repo.getInstance();
        if (repo!=null&&repo.getCustomers()!=null) {
            for (int i = 0; i < repo.getCustomers().size(); ++i) {
                if (repo.getCustomers().get(i).getId() != null &&
                        repo.getCustomers().get(i).getId().equals(specID))
                    customer = repo.getCustomers().get(i);
                return;
            }
        }
        customer=new Customer(specID);
    }

}
